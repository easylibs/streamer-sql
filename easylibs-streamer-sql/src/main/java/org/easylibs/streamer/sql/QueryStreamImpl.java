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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Spliterator;
import java.util.function.Consumer;

import org.easylibs.streamer.sql.util.ResultSetSpliterator;
import org.easylibs.streamer.tuple.Tuple;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
class QueryStreamImpl<T> extends AbstractStream<T> implements SqlQueryStream<T> {

	private final QueryBuilderImpl<T> builder;

	private final Connection connection;

	private Consumer<String> onSqlAction = i -> {
	};

	@SuppressWarnings("unchecked")
	private Class<T> type = (Class<T>) Tuple.class;

	public QueryStreamImpl(Connection connection) {
		this.connection = connection;
		builder = new QueryBuilderImpl<>(connection);
	}

	@Override
	protected Spliterator<T> createSpliterator() {
		return new ResultSetSpliterator<>(executeQuery(), type);
	}

	/**
	 * @return
	 */
	private ResultSet executeQuery() {

		final String sql = builder.toSql();

		onSqlAction.accept(sql);

		try {
			return connection.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			throw new StreamerSqlException(e);
		}
	}

	public SqlQueryStream<T> from(String... tables) {
		builder.from(tables);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQueryStream#groupBy(java.lang.String[])
	 */
	@Override
	public SqlQueryStream<T> groupBy(String arg, String... args) {
		builder.groupBy(arg, args);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQueryStream#having(java.lang.String)
	 */
	@Override
	public SqlQueryStream<T> having(String expression) {
		builder.having(expression);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQueryStream#limit(long)
	 */
	@Override
	public SqlQueryStream<T> limit(long limit) {
		builder.limit(limit);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQueryStream#limit(long, long)
	 */
	@Override
	public SqlQueryStream<T> limit(long offset, long limit) {
		builder.limit(offset, limit);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQueryStream#limit(java.lang.String)
	 */
	@Override
	public SqlQueryStream<T> limit(String limit) {
		builder.limit(limit);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQueryStream#limit(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public SqlQueryStream<T> limit(String offset, String limit) {
		builder.limit(offset, limit);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQueryStream#offset(long)
	 */
	@Override
	public SqlQueryStream<T> offset(long offset) {
		builder.offset(offset);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQueryStream#offset(java.lang.String)
	 */
	@Override
	public SqlQueryStream<T> offset(String offset) {
		builder.offset(offset);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQueryStream#orderBy(java.lang.String[])
	 */
	@Override
	public SqlQueryStream<T> orderBy(String arg, String... args) {
		builder.orderBy(arg, args);

		return this;
	}

	@Override
	public SqlQueryStream<T> peekSql(Consumer<String> action) {
		this.onSqlAction = this.onSqlAction.andThen(action);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQueryStream#select(java.lang.Class,
	 *      java.lang.String[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <U> SqlQueryStream<U> select(Class<U> type, String arg, String... args) {
		builder.select(type, arg, args);

		this.type = (Class<T>) type;
		return (SqlQueryStream<U>) this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQueryStream#select(java.lang.String[])
	 */
	@Override
	public SqlQueryStream<T> select(String arg, String... args) {
		builder.select(arg, args);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQueryStream#where(java.lang.String)
	 */
	@Override
	public SqlQueryStream<T> where(String expression) {
		builder.where(expression);

		return this;
	}

}
