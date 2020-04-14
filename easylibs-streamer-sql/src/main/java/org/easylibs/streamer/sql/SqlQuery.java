package org.easylibs.streamer.sql;

import java.sql.SQLException;

import org.easylibs.streamer.HasStream;

public interface SqlQuery<E> extends HasStream<E> {

	public interface QueryBuilder<E> {

		<U> QueryBuilder<U> select(Class<U> type, String... args);

		QueryBuilder<E> select(String... args);

		QueryBuilder<E> where(String expression);

		QueryBuilder<E> having(String expression);

		QueryBuilder<E> groupBy(String... args);

		QueryBuilder<E> orderBy(String... args);

		QueryBuilder<E> limit(long limit);

		QueryBuilder<E> limit(String limit);

		QueryBuilder<E> limit(String offset, String limit);

		QueryBuilder<E> limit(long offset, long limit);

		QueryBuilder<E> offset(long offset);

		QueryBuilder<E> offset(String offset);

		SqlQuery<E> build() throws SQLException;

	}

	PreparedSqlQuery<E> prepare();

	SqlQueryStream<E> stream();

}
