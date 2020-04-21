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
import java.util.function.Consumer;

import org.easylibs.streamer.builder.StreamerException;
import org.easylibs.streamer.tuple.Tuple;

/**
 * A spliterator that fetches all of its data from a {@code java.sql.ResultSet}.
 * Any {@code SQLException} errors are thrown as {@code StreamerSqlException}
 * which is unchecked.
 *
 * @param <T> the generic type, that unless specifically requested will be of
 *            type {@link Tuple}
 * @see SqlRegistry
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class ResultSetSpliterator<T> extends DelegateSpliterator<T> {

	private final SqlGetter<T> getter;
	private final ResultSet rs;

	/**
	 * Instantiates a new result set spliterator of type {@link Tuple}. All columns
	 * in each resultset are read and returned in {@link Tuple} POJOs.
	 *
	 * @param rs the resultset to read from
	 */
	@SuppressWarnings({ "unchecked" })
	public ResultSetSpliterator(ResultSet rs) {
		this(rs, (SqlGetter<T>) SqlRegistry.global().get(Tuple.class));
	}

	/**
	 * Instantiates a new result set spliterator of type {@link Tuple}. All columns
	 * in each resultset are read using the supplied {@code getter}.
	 *
	 * @param rs     the rs
	 * @param getter which reads all the data from resultset and returns object of
	 *               type {@code <T>}
	 */
	public ResultSetSpliterator(ResultSet rs, SqlGetter<T> getter) {
		super(0);
		this.rs = rs;
		this.getter = getter;
	}

	/**
	 * Instantiates a new result set spliterator of type {@link <T>} using
	 * preregistered {@link SqlGetter}. All columns in each resultset are read using
	 * a globally registered {@code getter} for data type {@code <T>}.
	 * 
	 * <h2>Resolving data type</h2>
	 * 
	 * The getter is resolved using {@link SqlRegistry#get(Class)} call. The
	 * registery has default bindings for all standard JDBC data types and all the
	 * java primitives.
	 * 
	 * 
	 * @param rs   the rs
	 * @param type the type
	 */
	public ResultSetSpliterator(ResultSet rs, Class<T> type) {
		this(rs, SqlRegistry.global().get(type));
	}

	/**
	 * Instantiates a new result set spliterator of type {@link <T>} using
	 * preregistered {@link SqlGetter}. All columns in each resultset are read using
	 * a globally registered {@code getter} for the supplied {@code sqlType}.
	 * 
	 * <h2>Resolving data type</h2>
	 * 
	 * The getter is resolved using {@link SqlRegistry#getSqlType(int)} call. The
	 * registery has default bindings for all standard JDBC data types and all the
	 * java primitives.
	 *
	 * @param rs      the rs
	 * @param sqlType the sql type
	 */
	public ResultSetSpliterator(ResultSet rs, int sqlType) {
		this(rs, SqlRegistry.global().getSqlType(sqlType));
	}

	/**
	 * Tries to advance the {@link ResultSet} cursor to the next data row and using
	 * the mapped {@code SqlGetter} fetch the column data.
	 *
	 * @param action the action
	 * @return true, if successful
	 * @see org.easylibs.streamer.sql.util.DelegateSpliterator#tryAdvance(java.util.function.Consumer)
	 */
	@Override
	public boolean tryAdvance(Consumer<? super T> action) {

		try {
			if (!rs.next()) {
				return false;
			}

			final T t = getter.read(rs);

			action.accept(t);

			return true;

		} catch (SQLException e) {
			throw new StreamerException(e);
		}

	}
}
