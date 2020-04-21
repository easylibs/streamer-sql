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

/**
 * A getter interface suitable for lambda expressions that retrieves one or more
 * values from a {@code ResultSet} row.
 *
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public interface SqlGetter<T> {

	/**
	 * Reads data from column 1 from a java sql {@code ResultSet}.
	 *
	 * @param rs the result set
	 * @return the read data, possibly as a Tuple or getter specific return type
	 * @throws SQLException any SQL exceptions while reading data from the
	 *                      {@code ResultSet}
	 */
	default T read(ResultSet rs) throws SQLException {
		return read(1, rs);
	}

	/**
	 * Reads data for specified column froma java sql {@code ResultSet}.
	 *
	 * @param colIndex the column index
	 * @param rs       the result set
	 * @return the read data, possibly as a Tuple or getter specific return type
	 * @throws SQLException any SQL exceptions while reading data from the
	 *                      {@code ResultSet}
	 */
	T read(int colIndex, ResultSet rs) throws SQLException;
}
