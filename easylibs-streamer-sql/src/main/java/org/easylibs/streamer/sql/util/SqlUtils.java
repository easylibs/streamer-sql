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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.easylibs.streamer.tuple.Tuple;

/**
 * SQL utility methods supporting various operations for the library.
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class SqlUtils {

	/**
	 * 
	 */
	public SqlUtils() {
	}

	/**
	 * Surround the supplied string with a backquote tick.
	 *
	 * @param str the string
	 * @return the quoted string
	 */
	public static String bq(String str) {
		return "`" + str + "`";
	}

	/**
	 * Replace all underscores with spaces.
	 *
	 * @param constant the constant
	 * @return the string
	 */
	public static String replaceUnderscores(Enum<?> constant) {
		return constant.name().replaceAll("_", " ");
	}

	/**
	 * Remove all underscores and replace with nothing.
	 *
	 * @param constant the constant
	 * @return the string
	 */
	public static String stripUnderscores(Enum<?> constant) {
		return constant.name().replaceAll("_", "");
	}

	@SuppressWarnings("unused")
	private static final String[] TEST_DATA = {
			"`film_id`",
			"`title`",
			"`description`",
			"`release_year`",
			"`language_id`",
			"`original_language_id`",
			"`rental_duration`",
			"`rental_rate`",
			"`length`",
			"`replacement_cost`",
			"`rating`",
			"`special_features`",
			"`last_update`",
	};

	/**
	 * Reads all the result set columns for the current row into a {@code Tuple}
	 * POJO.
	 *
	 * @param <T>   Tuple type
	 * @param rs    the result set
	 * @param tuple the tuple to read the result set row data into
	 * @throws SQLException any SQL exceptions while reading data from the result
	 *                      set
	 */
	public static <T extends Tuple> T readIntoTuple(ResultSet rs, final T tuple) throws SQLException {

		final ResultSetMetaData meta = rs.getMetaData();

		for (int i = 0; i < tuple.degree(); i++) {

			final Object value = rs.getObject(i + 1);
			final String label = meta.getColumnLabel(i + 1);

			tuple.set(i, value);
			tuple.setLabel(i, label);

		}

		return tuple;
	}

}
