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

import org.easylibs.streamer.HasSql;
import org.easylibs.streamer.builder.StreamerBuilder;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
abstract class SqlBuilder<E extends Enum<E>, T> extends StreamerBuilder<E, T> implements HasSql {

	/**
	 * 
	 */
	public SqlBuilder() {
	}

	/**
	 * @param link
	 */
	public SqlBuilder(StreamerBuilder<E, T> link) {
		super(link);
	}

	/**
	 * @see org.easylibs.streamer.builder.StreamerBuilder#buildSql()
	 */
	public abstract String buildSql();

	/**
	 * @see org.easylibs.streamer.sql.HasSql#toSql()
	 */
	@Override
	public String toSql() {
		return buildSql();
	}

}
