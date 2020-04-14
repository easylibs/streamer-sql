package org.easylibs.streamer.sql;

import java.util.stream.Stream;

public interface SqlQueryStream<E> extends Stream<E> {

	SqlQueryStream<E> select(String... args);

	<U> SqlQueryStream<U> select(Class<U> type, String... args);

	SqlQueryStream<E> where(String expression);

	SqlQueryStream<E> having(String expression);

	SqlQueryStream<E> groupBy(String... args);

	SqlQueryStream<E> orderBy(String... args);

	SqlQueryStream<E> limit(long limit);

	SqlQueryStream<E> limit(String limit);

	SqlQueryStream<E> limit(String offset, String limit);

	SqlQueryStream<E> limit(long offset, long limit);

	SqlQueryStream<E> offset(long offset);

	SqlQueryStream<E> offset(String offset);

}
