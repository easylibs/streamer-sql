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

import java.sql.SQLException;

import org.easylibs.streamer.builder.StreamerException;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
public class StreamerSqlException extends StreamerException {

	private static final long serialVersionUID = 730259575580610546L;

	public StreamerSqlException() {
	}

	/**
	 * @param arg0
	 */
	public StreamerSqlException(SQLException arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public StreamerSqlException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public StreamerSqlException(String arg0, SQLException arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public StreamerSqlException(String arg0, SQLException arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	@Override
	public synchronized SQLException getCause() {
		return (SQLException) super.getCause();
	}

}
