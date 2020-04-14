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

import org.easylibs.streamer.sql.SqlDelete.DeleteBuilder;
import org.easylibs.streamer.sql.SqlInsert.InsertBuilder;
import org.easylibs.streamer.sql.SqlJoin.JoinBuilder;
import org.easylibs.streamer.sql.SqlQuery.QueryBuilder;
import org.easylibs.streamer.sql.SqlUpdate.UpdateBuilder;
import org.easylibs.streamer.tuple.Tuple;

public interface SqlStreamer {

	static SqlStreamer of(Connection connection) {
		throw new UnsupportedOperationException();
	}

	SqlStreamer beginTransaction() throws SQLException;

	DeleteBuilder deleteBuilder(String tableReference);

	SqlStreamer endTransaction() throws SQLException;

	Connection getConnection() throws SQLException;

	InsertBuilder insertBuilder(String tableReference, String... columnReferences);

	JoinBuilder<Tuple> joinBuilder();

	/**
	 * Query in the form where there is not "FROM" in the SELECT statement. Used in
	 * executing raw SELECT commands or quering backend paremeters and state.
	 * 
	 * <p>
	 * For example: <code>
	 * </pre>
	 * sql shell: SELECT 1 + 1;
	 * </pre>
	 * </code>
	 * </p>
	 *
	 * @return the query stream
	 */
	SqlQueryStream<Tuple> query();

	/**
	 * Create and execute a query. The {@code tableReference(s)} (i.e. FROM
	 * tables...) are used to select the tables on which the query will be applied.
	 *
	 * @param tableReference  the table reference (FROM tables)
	 * @param tableReferences the table references (FROM tables), added to the above
	 *                        list
	 * @return the query stream with the result set
	 */
	SqlQueryStream<Tuple> query(String tableReference, String... tableReferences);

	/**
	 * A query builder in the form where there is not "FROM" in the SELECT
	 * statement. Used in executing raw SELECT commands or quering backend
	 * paremeters and state.
	 * 
	 * <p>
	 * For example: <code>
	 * </pre>
	 * sql shell: SELECT 1 + 1;
	 * </pre>
	 * </code>
	 * </p>
	 *
	 * @return the query builder to build the query with
	 */
	QueryBuilder<Tuple> queryBuilder();

	/**
	 * Create a query builder. The {@code tableReference(s)} (i.e. FROM tables...)
	 * are used to select the tables on which the query will be applied.
	 *
	 * @param tableReference  the table reference (FROM tables)
	 * @param tableReferences the table references (FROM tables), added to the above
	 *                        list
	 *
	 * @param tableReference  the table reference
	 * @param tableReferences the table references
	 * @return the query builder
	 */
	QueryBuilder<Tuple> queryBuilder(String tableReference, String... tableReferences);

	UpdateBuilder updateBuilder(String tableReference, String... tableReferences);

	PreparedSqlQuery<Tuple> using(PreparedStatement statement, PreparedSqlAction executeAction)
			throws SQLException;

}
