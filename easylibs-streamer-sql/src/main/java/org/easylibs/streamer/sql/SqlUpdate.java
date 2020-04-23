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

/**
 * The Interface SqlUpdate.
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface SqlUpdate {

	/**
	 * The Interface Builder.
	 */
	public interface Builder extends HasSql {

		/**
		 * Builds the.
		 *
		 * @return the sql update
		 */
		SqlUpdate build();

		/**
		 * Execute.
		 *
		 * @return the int
		 */
		int execute();

		/**
		 * Prepare.
		 *
		 * @return the prepared sql update
		 */
		PreparedSqlUpdate prepare();

		/**
		 * Limit.
		 *
		 * @param limit the limit
		 * @return the builder
		 */
		Builder limit(long limit);

		/**
		 * Limit.
		 *
		 * @param limit the limit
		 * @return the builder
		 */
		Builder limit(String limit);

		/**
		 * Order by.
		 *
		 * @param orderBy the order by
		 * @return the builder
		 */
		Builder orderBy(String orderBy);

		/**
		 * Sets the.
		 *
		 * @param arg  the arg
		 * @param args the args
		 * @return the builder
		 */
		Builder set(String arg, String... args);

		/**
		 * Where.
		 *
		 * @param where the where
		 * @return the builder
		 */
		Builder where(String where);

		/**
		 * Peek sql.
		 *
		 * @param action the action
		 * @return the builder
		 */
		Builder peekSql(Consumer<String> action);
	}

	/**
	 * Prepare.
	 *
	 * @return the prepared sql update
	 */
	PreparedSqlUpdate prepare();

	/**
	 * Execute.
	 *
	 * @return the int
	 */
	int execute();
}
