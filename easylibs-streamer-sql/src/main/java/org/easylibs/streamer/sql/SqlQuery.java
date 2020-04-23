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

import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.easylibs.streamer.HasStream;

/**
 * The Interface SqlQuery.
 *
 * @param <E> the element type
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface SqlQuery<E> extends HasStream<E>, HasSql {

	/**
	 * The Interface Builder.
	 *
	 * @param <E> the element type
	 */
	public interface Builder<E> extends HasSql {

		/**
		 * Select.
		 *
		 * @param <U>  the generic type
		 * @param type the type
		 * @param arg  the arg
		 * @param args the args
		 * @return the builder
		 */
		<U> Builder<U> select(Class<U> type, String arg, String... args);

		/**
		 * Select.
		 *
		 * @param arg  the arg
		 * @param args the args
		 * @return the builder
		 */
		Builder<E> select(String arg, String... args);

		/**
		 * Where.
		 *
		 * @param expression the expression
		 * @return the builder
		 */
		Builder<E> where(String expression);

		/**
		 * Having.
		 *
		 * @param expression the expression
		 * @return the builder
		 */
		Builder<E> having(String expression);

		/**
		 * Group by.
		 *
		 * @param arg  the arg
		 * @param args the args
		 * @return the builder
		 */
		Builder<E> groupBy(String arg, String... args);

		/**
		 * Order by.
		 *
		 * @param arg  the arg
		 * @param args the args
		 * @return the builder
		 */
		Builder<E> orderBy(String arg, String... args);

		/**
		 * Limit.
		 *
		 * @param limit the limit
		 * @return the builder
		 */
		Builder<E> limit(long limit);

		/**
		 * Limit.
		 *
		 * @param limit the limit
		 * @return the builder
		 */
		Builder<E> limit(String limit);

		/**
		 * Limit.
		 *
		 * @param offset the offset
		 * @param limit  the limit
		 * @return the builder
		 */
		Builder<E> limit(String offset, String limit);

		/**
		 * Limit.
		 *
		 * @param offset the offset
		 * @param limit  the limit
		 * @return the builder
		 */
		Builder<E> limit(long offset, long limit);

		/**
		 * Offset.
		 *
		 * @param offset the offset
		 * @return the builder
		 */
		Builder<E> offset(long offset);

		/**
		 * Offset.
		 *
		 * @param offset the offset
		 * @return the builder
		 */
		Builder<E> offset(String offset);

		/**
		 * Distinct.
		 *
		 * @return the builder
		 */
		Builder<E> distinct();

		/**
		 * Peek sql.
		 *
		 * @param action the action
		 * @return the builder
		 */
		Builder<E> peekSql(Consumer<String> action);

		/**
		 * Builds the.
		 *
		 * @return the sql query
		 * @throws SQLException the SQL exception
		 */
		SqlQuery<E> build() throws SQLException;

		/**
		 * @see org.easylibs.streamer.sql.HasSql#toSql()
		 */
		String toSql();

	}

	/**
	 * Prepare.
	 *
	 * @return the prepared sql query
	 */
	PreparedSqlQuery<E> prepare();

	/**
	 * @see org.easylibs.streamer.HasStream#stream()
	 */
	Stream<E> stream();

}
