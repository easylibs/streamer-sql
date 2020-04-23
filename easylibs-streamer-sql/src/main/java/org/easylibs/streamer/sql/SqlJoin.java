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

import org.easylibs.streamer.HasStream;

/**
 * The Interface SqlJoin.
 *
 * @param <E> the element type
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
public interface SqlJoin<E> extends HasStream<E> {

	/**
	 * The Interface Builder.
	 *
	 * @param <E> the element type
	 */
	public interface Builder<E> extends HasSql {

		/**
		 * Inner join on.
		 *
		 * @param tableReference     the table reference
		 * @param joinTableReference the join table reference
		 * @param searchcondition    the searchcondition
		 * @return the builder
		 */
		Builder<E> innerJoinOn(String tableReference, String joinTableReference, String searchcondition);

		/**
		 * Inner join using.
		 *
		 * @param tableReference     the table reference
		 * @param joinTableReference the join table reference
		 * @param firstJoinColumn    the first join column
		 * @param moreJoinColumns    the more join columns
		 * @return the builder
		 */
		Builder<E> innerJoinUsing(String tableReference,
				String joinTableReference,
				String firstJoinColumn,
				String... moreJoinColumns);

		/**
		 * Cross join using.
		 *
		 * @param tableReference     the table reference
		 * @param joinTableReference the join table reference
		 * @param firstJoinColumn    the first join column
		 * @param moreJoinColumns    the more join columns
		 * @return the builder
		 */
		Builder<E> crossJoinUsing(String tableReference,
				String joinTableReference,
				String firstJoinColumn,
				String... moreJoinColumns);

		/**
		 * Join on.
		 *
		 * @param tableReference     the table reference
		 * @param joinTableReference the join table reference
		 * @param firstJoinColumn    the first join column
		 * @param moreJoinColumns    the more join columns
		 * @return the builder
		 */
		Builder<E> joinOn(String tableReference,
				String joinTableReference,
				String firstJoinColumn,
				String... moreJoinColumns);

	}

	/**
	 * Prepare.
	 *
	 * @return the prepared sql query
	 */
	PreparedSqlQuery<E> prepare();

}
