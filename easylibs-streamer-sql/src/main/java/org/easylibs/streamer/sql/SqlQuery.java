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

public interface SqlQuery<E> extends HasStream<E>, HasSql {

	public interface Builder<E> extends HasSql {

		<U> Builder<U> select(Class<U> type, String arg, String... args);

		Builder<E> select(String arg, String... args);

		Builder<E> where(String expression);

		Builder<E> having(String expression);

		Builder<E> groupBy(String arg, String... args);

		Builder<E> orderBy(String arg, String... args);

		Builder<E> limit(long limit);

		Builder<E> limit(String limit);

		Builder<E> limit(String offset, String limit);

		Builder<E> limit(long offset, long limit);

		Builder<E> offset(long offset);

		Builder<E> offset(String offset);

		Builder<E> distinct();

		Builder<E> peekSql(Consumer<String> action);

		SqlQuery<E> build() throws SQLException;

		String toSql();

	}

	PreparedSqlQuery<E> prepare();

	Stream<E> stream();

}
