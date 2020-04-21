/*
 * MIT License
 * 
 * Copyright (c) 2020 Sly Technologies Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.easylibs.streamer.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.easylibs.streamer.sql.SqlDelete.Builder;
import org.easylibs.streamer.sql.function.SqlBiConsumer;
import org.easylibs.streamer.sql.function.SqlConsumer;
import org.easylibs.streamer.tuple.Tuple;

/**
 * SQL Streamer providing many JDBC related operations.
 * <p>
 * The following operations are provided by the base {@code SqlStreamer}:
 * <ul>
 * <li>{@link #createDatabase} - creates a new database</li>
 * <li>{@link #createTable} - creates a new database table</li>
 * <li>{@link #insert} - inserts a data row into a database table</li>
 * <li>{@link #delete} - deletes data rows from a database table</li>
 * <li>{@link #update} - updates data columns in a database table</li>
 * <li>{@link #query} - retrieves data from one or more database tables or
 * performs a pure query</li>
 * <li>{@link #join} - performs a database join on multiple tables</li>
 * </ul>
 * </p>
 * <h3>Transactions</h3>
 * <p>
 * Transaction based operations are also supported:
 * <ul>
 * <li>{@link #beginTransaction()} - marks the beginning of a set of related
 * transactions</li>
 * <li>{@link #endTransaction()} - commits all the transactions atomically,
 * ensuring they all succeed</li>
 * <li>{@link #rollbackTransaction()} - rolls back all database transactions
 * between the last call to {@link #beginTransaction()} and
 * {@link #endTransaction()} if the {@link #endTransaction()} call failed
 * </ul>
 * Transactions allow multiple operations to be submitted but not committed to
 * the database. The complete transaction set is committed atomically, ensuring
 * that all or none of the transactions succeed.
 * </p>
 * <p>
 * On success, the {@link #endTransaction()} call returns normally and all of
 * the transactions are guarranteed to have been committed to the database.
 * </p>
 * <p>
 * On failure, all transactions are rolled back or undone, returning database to
 * a state before the initial {@link #beginTransaction()} call.
 * </p>
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class SqlStreamer {

	private final Connection connection;

	private boolean savedAutoCommit;

	private SqlDialect dialect = SqlDialect.basic();

	SqlStreamer(Connection connection) {
		this.connection = connection;

		try {
			this.savedAutoCommit = connection.getAutoCommit();
		} catch (SQLException e) {
			throw new StreamerSqlException(e);
		}
	}

	/**
	 * @throws SQLException
	 * 
	 */
	SqlStreamer(Connection connection, SqlDialect dialect) {
		this.connection = connection;
		this.setDialect(dialect);

		try {
			this.savedAutoCommit = connection.getAutoCommit();
		} catch (SQLException e) {
			throw new StreamerSqlException(e);
		}
	}

	/**
	 * Begin a transaction where no data are permanently commited to the databased
	 * until a matching {@link #endTransaction()} is called. Each
	 * {@link #beginTransaction()} must have a matching {@link #endTransaction()}
	 * call made.
	 *
	 * @return this SQL streamer
	 * @throws SQLException Any SQL errors
	 */
	public SqlStreamer beginTransaction() throws SQLException {

		connection.setAutoCommit(false);

		return this;
	}

	private SqlStreamer commit() throws SQLException {

		if (!connection.getAutoCommit()) {
			connection.commit();
		}

		return this;
	}

	public SqlCreateDatabase.Builder createDatabase(String dbName) {
		throw new UnsupportedOperationException();
	}

	public SqlCreateTable.Builder createTable(String tableName) {
		throw new UnsupportedOperationException();
	}

	public Builder delete(String tableReference) {
		return new DeleteBuilderImpl(connection, tableReference);
	}

	/**
	 * Makes all changes made since the previous
	 * beginTransaction/rollbackTransaction permanent and releases any database
	 * locks currently held by this Streamer object. This method should be used only
	 * after a matching beginTransaction has been called.
	 *
	 * @return the sql streamer
	 * @throws SQLException if a database access error occurs, this method is called
	 *                      while participating in a distributed transaction, this
	 *                      method is called on a closed connection or this
	 *                      Connection object is in auto-commit mode
	 */
	public SqlStreamer endTransaction() throws SQLException {

		try {
			return commit();
		} finally {
			connection.setAutoCommit(savedAutoCommit);
		}
	}

	/**
	 * Makes all changes made since the previous
	 * beginTransaction/rollbackTransaction permanent and releases any database
	 * locks currently held by this Streamer object. This method should be used only
	 * after a matching beginTransaction has been called.
	 *
	 * @param rollbackAction the action is called if an SQL exception was raised
	 *                       while attempting to commit the data to database.
	 * @return the sql streamer
	 * @throws SQLException if a database access error occurs, this method is called
	 *                      while participating in a distributed transaction, this
	 *                      method is called on a closed connection or this
	 *                      Connection object is in auto-commit mode
	 */
	public SqlStreamer endTransaction(SqlBiConsumer<SqlStreamer, SQLException> rollbackAction)
			throws SQLException {

		try {

			return endTransaction();

		} catch (SQLException e) {

			rollbackAction.accept(this, e);

			return this;
		}

	}

	/**
	 * Gets the currently active database SQL connection.
	 *
	 * @return the active connection
	 * @throws SQLException the SQL exception
	 */
	public Connection getConnection() throws SQLException {
		return connection;
	}

	SqlDialect getDialect() {
		return dialect;
	}

	public SqlInsert.Builder insert(String tableReference) {
		return new InsertBuilderImpl(getDialect(), connection, tableReference);
	}

	public <T> SqlJoin.Builder<T> join(String tableReference) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Create and execute a query. The {@code tableReference(s)} (i.e. FROM
	 * tables...) are used to select the tables on which the query will be applied.
	 *
	 * @param tableReferences the table references (FROM tables), added to the above
	 *                        list
	 * @return the query stream with the result set
	 */
	@SuppressWarnings("resource")
	public SqlQueryStream<Tuple> query(String... tableReferences) {
		return new QueryStreamImpl<Tuple>(connection)
				.from(tableReferences)
				.select("*");
	}

	/**
	 * Create a query builder. The {@code tableReference(s)} (i.e. FROM tables...)
	 * are used to select the tables on which the query will be applied.
	 * 
	 * <p>
	 * When no table references are provided, the query is executed without a
	 * {@code FROM} component allowing basic queries such as {@code SELECT 1 + 1} to
	 * be executed.
	 * <p>
	 * 
	 * <p>
	 * <b>Implementation note:</b> not all databases support no table reference
	 * queries.
	 * </p>
	 *
	 * @param tableReferences the table references (FROM tables), added to the above
	 *                        list
	 * @return the query builder
	 */
	public SqlQuery.Builder<Tuple> queryBuilder(String... tableReferences) {
		return new QueryBuilderImpl<>(connection, tableReferences);
	}

	/**
	 * Rollback transaction.
	 *
	 * @return the sql streamer
	 * @throws SQLException if a database access error occurs, this method is called
	 *                      while participating in a distributed transaction, this
	 *                      method is called on a closed connection or this
	 *                      Connection object is in auto-commit mode
	 */
	public SqlStreamer rollbackTransaction() throws SQLException {

		try {
			connection.rollback();

			return this;
		} finally {
			connection.setAutoCommit(savedAutoCommit);
		}
	}

	void setDialect(SqlDialect dialect) {
		this.dialect = dialect;
	}

	public SqlUpdate.Builder update(String tableReference) {
		return new UpdateBuilderImpl(connection, tableReference);
	}

	public PreparedSqlQuery<Tuple> using(PreparedStatement statement, SqlConsumer<PreparedStatement> executeAction)
			throws SQLException {
		throw new UnsupportedOperationException("");
	}

}
