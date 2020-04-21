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

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.easylibs.streamer.sql.util.ResultSetIterator;
import org.easylibs.streamer.sql.util.ResultSetSpliterator;
import org.easylibs.streamer.tuple.Tuple;

/**
 * Support class providing factory methods for creation of {@link SqlStreamer}
 * and other related objects.
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public final class SqlStreamerSupport {

	/**
	 * 
	 */
	private SqlStreamerSupport() {
	}

	/**
	 * Creates a SQL connected streamer.
	 *
	 * @param connection existing SQL connection
	 * @return the SQL streamer
	 */
	public static SqlStreamer streamer(Connection connection) {
		return new SqlStreamer(connection);
	}

	public static Stream<Tuple> stream(ResultSet rs) {
		return StreamSupport.stream(new ResultSetSpliterator<>(rs), false);
	}

	public static <T> Stream<T> stream(ResultSet rs, Class<T> type) {
		return StreamSupport.stream(new ResultSetSpliterator<>(rs, type), false);
	}

	public static Iterable<Tuple> iterable(ResultSet rs) {
		return () -> new ResultSetIterator<>(rs);
	}

	public static <T> Iterable<T> iterable(ResultSet rs, Class<T> type) {
		return () -> new ResultSetIterator<>(rs, type);
	}

	public static Iterator<Tuple> iterator(ResultSet rs) {
		return new ResultSetIterator<>(rs);
	}

	public static <T> Iterator<T> iterator(ResultSet rs, Class<T> type) {
		return new ResultSetIterator<>(rs, type);
	}

}
