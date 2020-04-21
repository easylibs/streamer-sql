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

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface PreparedSqlUpdate extends PreparedSql<PreparedSqlUpdate> {

	int execute() throws StreamerSqlException;

	default <K> Stream<K> setValueAndExecute(Object value) throws StreamerSqlException {
		return setNextValue(value).stream();
	}

	/**
	 * Returns a stream of auto-generated keys from a prepared statement. If no
	 * auto-generated keys/default values are returned, then an
	 * {@link Stream#empty()} is returned.
	 * <p>
	 * Note you must specific {@code true} to {@link SqlInsert#prepare(boolean)}
	 * method to request that JDBC return the auto-generated keys.
	 * </p>
	 *
	 * @param <K> the key type
	 * @return the stream
	 */
	<K> Stream<K> stream();

	@SuppressWarnings("unchecked")
	default <K> void forEach(Consumer<K> action) {
		((Stream<K>) stream()).forEach(action);
	}
}
