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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.easylibs.streamer.sql.util.ResultSetSpliterator;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
class PreparedQueryImpl<E> extends AbstractPrepared<PreparedSqlQuery<E>>
		implements PreparedSqlQuery<E> {

	public PreparedQueryImpl(PreparedStatement statement, String sql) {
		super(statement, sql);
	}

	private ResultSet executeQuery() {
		try {
			return getStatement().executeQuery();
		} catch (SQLException e) {
			throw new StreamerSqlException(e);
		}
	}

	/**
	 * @see org.easylibs.streamer.HasStream#stream()
	 */
	@Override
	public Stream<E> stream() {
		return StreamSupport.stream(new ResultSetSpliterator<>(executeQuery()), false);
	}

}
