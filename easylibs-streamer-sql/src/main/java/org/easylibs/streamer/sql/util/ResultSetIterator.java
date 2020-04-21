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
package org.easylibs.streamer.sql.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.easylibs.streamer.sql.StreamerSqlException;
import org.easylibs.streamer.tuple.Tuple;

/**
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class ResultSetIterator<T> implements Iterator<T> {

	private final ResultSet rs;
	private final SqlGetter<T> getter;

	@SuppressWarnings("unchecked")
	public ResultSetIterator(ResultSet rs) {
		this(rs, (SqlGetter<T>) SqlRegistry.global().get(Tuple.class));
	}

	public ResultSetIterator(ResultSet rs, Class<T> type) {
		this(rs, SqlRegistry.global().get(type));
	}

	public ResultSetIterator(ResultSet rs, SqlGetter<T> getter) {
		this.rs = rs;
		this.getter = getter;
	}

	public ResultSetIterator(ResultSet rs, int sqlType) {
		this(rs, SqlRegistry.global().getSqlType(sqlType));
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new StreamerSqlException(e);
		}
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	@Override
	public T next() {
		try {

			final T t = getter.read(rs);

			return t;
		} catch (SQLException e) {
			throw new StreamerSqlException(e);
		}

	}

}
