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
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.easylibs.streamer.sql.util.ResultSetSpliterator;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
class PreparedUpdateImpl extends AbstractPrepared<PreparedSqlUpdate> implements PreparedSqlUpdate {

	private boolean isExecuted;

	/**
	 * @param statement
	 * @param sql
	 */
	public PreparedUpdateImpl(PreparedStatement statement, String sql) {
		super(statement, sql);
	}

	/**
	 * @see org.easylibs.streamer.sql.PreparedSqlUpdate#execute()
	 */
	@Override
	public int execute() throws StreamerSqlException {

		int result = executeUpdate();

		reset();
		isExecuted = true;

		return result;
	}

	private int executeUpdate() throws StreamerSqlException {
		try {
			return getStatement().executeUpdate();
		} catch (SQLException e) {
			throw new StreamerSqlException(e);
		}
	}

	protected PreparedSqlUpdate reset() {
		this.isExecuted = false;

		return super.reset();
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlInsert#stream()
	 */
	@Override
	public <K> Stream<K> stream() {
		if (!isExecuted) {
			execute();

			isExecuted = false; // Prepare for next execution
		}

		try {
			final ResultSet rs = getStatement().getGeneratedKeys();

			if (rs == null) {
				return Stream.empty();
			}

			Spliterator<K> spliterator = new ResultSetSpliterator<K>(rs, rs.getMetaData().getColumnType(1));
			return StreamSupport.stream(spliterator, false);
		} catch (SQLException e) {
			throw new StreamerSqlException(e);
		}
	}

	@Override
	public String toString() {
		return "PreparedUpdate [sql=" + toSql() + "]";
	}

}
