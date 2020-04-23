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

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * The Interface SqlQueryStream.
 *
 * @param <E> the element type
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface SqlQueryStream<E> extends Stream<E> {

	/**
	 * Select.
	 *
	 * @param arg  the arg
	 * @param args the args
	 * @return the sql query stream
	 */
	SqlQueryStream<E> select(String arg, String... args);

	/**
	 * Select.
	 *
	 * @param <U>  the generic type
	 * @param type the type
	 * @param arg  the arg
	 * @param args the args
	 * @return the sql query stream
	 */
	<U> SqlQueryStream<U> select(Class<U> type, String arg, String... args);

	/**
	 * Where.
	 *
	 * @param expression the expression
	 * @return the sql query stream
	 */
	SqlQueryStream<E> where(String expression);

	/**
	 * Having.
	 *
	 * @param expression the expression
	 * @return the sql query stream
	 */
	SqlQueryStream<E> having(String expression);

	/**
	 * Group by.
	 *
	 * @param arg  the arg
	 * @param args the args
	 * @return the sql query stream
	 */
	SqlQueryStream<E> groupBy(String arg, String... args);

	/**
	 * Order by.
	 *
	 * @param arg  the arg
	 * @param args the args
	 * @return the sql query stream
	 */
	SqlQueryStream<E> orderBy(String arg, String... args);

	/**
	 * @see java.util.stream.Stream#limit(long)
	 */
	SqlQueryStream<E> limit(long limit);

	/**
	 * Limit.
	 *
	 * @param limit the limit
	 * @return the sql query stream
	 */
	SqlQueryStream<E> limit(String limit);

	/**
	 * Limit.
	 *
	 * @param offset the offset
	 * @param limit  the limit
	 * @return the sql query stream
	 */
	SqlQueryStream<E> limit(String offset, String limit);

	/**
	 * Limit.
	 *
	 * @param offset the offset
	 * @param limit  the limit
	 * @return the sql query stream
	 */
	SqlQueryStream<E> limit(long offset, long limit);

	/**
	 * Offset.
	 *
	 * @param offset the offset
	 * @return the sql query stream
	 */
	SqlQueryStream<E> offset(long offset);

	/**
	 * Offset.
	 *
	 * @param offset the offset
	 * @return the sql query stream
	 */
	SqlQueryStream<E> offset(String offset);

	/**
	 * Peek sql.
	 *
	 * @param action the action
	 * @return the sql query stream
	 */
	SqlQueryStream<E> peekSql(Consumer<String> action);

}
