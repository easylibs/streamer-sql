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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
abstract class AbstractPrepared<P extends PreparedSql<P>> implements PreparedSql<P> {

	private static void ensureListSize(List<String> list, int size) {

		final int len = list.size();
		if (size >= len) {
			for (int i = 0; i < size - len; i++) {
				list.add(null);
			}
		}

	}

	private final PreparedStatement statement;
	private final String sql;
	private ArrayList<String> valuesList = new ArrayList<>();

	private int index = 0;

	public AbstractPrepared(PreparedStatement statement, String sql) {
		this.statement = statement;
		this.sql = sql;
	}

	/**
	 * Close the prepared statement
	 *
	 * @throws StreamerSqlException the streamer sql exception
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws StreamerSqlException {

		try {
			getStatement().close();
		} catch (SQLException e) {
			throw new StreamerSqlException(e);
		}

	}

	protected PreparedStatement getStatement() {
		return this.statement;
	}

	protected P reset() {
		this.index = 0;

		return us();
	}

	/**
	 * @see org.easylibs.streamer.sql.PreparedSql#setNextValue(java.lang.Object)
	 */
	@Override
	public P setNextValue(Object value) {
		setValueAt(++index, value);

		return us();
	}

	/**
	 * @see org.easylibs.streamer.sql.PreparedSql#setValueAt(int, java.lang.Object)
	 */
	@Override
	public P setValueAt(int index, Object value) {
		try {

			statement.setObject(index, value);

			ensureListSize(valuesList, index);

			valuesList.set(index - 1, value.toString());

			return us();
		} catch (SQLException e) {
			throw new StreamerSqlException(e);
		}
	}

	/**
	 * @see org.easylibs.streamer.sql.HasSql#toSql()
	 */
	@Override
	public String toSql() {

		if (valuesList.isEmpty()) {
			return sql;
		} else {
			return sql + "\n--values:" + valuesList;
		}

	}

	@Override
	public String toString() {
		return "PreparedSql [sql=" + sql + "]";
	}

	@SuppressWarnings("unchecked")
	private final P us() {
		return (P) this;
	}

}
