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

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
class BasicDialect implements SqlDialect {

	/**
	 * 
	 */
	public BasicDialect() {
	}

	public String quoteValue(Object value) {
		return String.valueOf(value);
	}

	public String[] quoteValues(Object... values) {
		final String[] result = new String[values.length];

		return Stream.of(values)
				.map(this::quoteValue)
				.collect(Collectors.toList())
				.toArray(result);
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlDialect#quoteColumn(java.lang.String)
	 */
	@Override
	public String quoteColumn(String column) {
		return column;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlDialect#quoteColumns(java.lang.String[])
	 */
	@Override
	public String[] quoteColumns(String... columns) {
		return Stream.of(columns)
				.map(this::quoteValue)
				.collect(Collectors.toList())
				.toArray(columns);
	}

	private static <E> Stream<E> concat(E first, E[] args) {
		if (args.length == 0) {
			return Stream.of(first);
		}
		return Stream.concat(Stream.of(first), Stream.of(args));
	}

	@Override
	public String groupColumns(String arg, String... args) {
		return concat(arg, args)
				.map(this::quoteColumn)
				.collect(Collectors.joining(", ", "(", ")"));

	}

	@Override
	public String groupValues(Object arg, Object... args) {
		return concat(arg, args)
				.map(this::quoteValue)
				.collect(Collectors.joining(", ", "(", ")"));
	}

	@Override
	public String quoteTable(String table) {
		return "'" + table + "'";

	}

}
