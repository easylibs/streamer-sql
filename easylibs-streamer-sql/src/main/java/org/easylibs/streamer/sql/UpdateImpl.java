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

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Sly Technologies Inc
 * @author repos@slytechs.com
 */
class UpdateImpl implements SqlUpdate, Closeable {

	private final Connection connection;
	private final String sql;
	private Statement statement;

	public UpdateImpl(Connection connection, String sql) {
		this.connection = connection;
		this.sql = sql;

	}

	/**
	 * @see org.easylibs.streamer.sql.SqlUpdate#prepare()
	 */
	@Override
	public PreparedSqlUpdate prepare() {
		try {
			return new PreparedUpdateImpl(connection.prepareStatement(sql), sql);
		} catch (SQLException e) {
			throw new StreamerSqlException(e);
		}
	}

	private int executeUpdate() throws StreamerSqlException {
		try {
			this.statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new StreamerSqlException(e);
		}
	}

	@Override
	public String toString() {
		return "Update [sql=" + sql + "]";
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlUpdate#execute()
	 */
	@Override
	public int execute() throws StreamerSqlException {
		return executeUpdate();
	}

	/**
	 * Close.
	 *
	 * @throws StreamerSqlException the streamer sql exception
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws StreamerSqlException {
		try {
			if (statement != null) {
				statement.close();
				statement = null;
			}
		} catch (SQLException e) {
			throw new StreamerSqlException(e);
		}

	}

}
