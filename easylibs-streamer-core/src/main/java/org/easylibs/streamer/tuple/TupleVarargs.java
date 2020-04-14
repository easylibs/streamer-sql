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
package org.easylibs.streamer.tuple;

public class TupleVarargs extends AbstractTuple {

	private static final long serialVersionUID = 7897029579218076365L;

	private Object[] values; // null indicates length of 0 or being serialized

	/**
	 * Special constructor needed for object serialization
	 */
	protected TupleVarargs() {
		super(-1);
	}

	public TupleVarargs(Object... values) {
		super(-1);

		this.values = values;
	}

	public TupleVarargs(int degree) {
		super(-1);

		values = new Object[degree];
	}

	@Override
	public int degree() {
		return ((values == null) ? 0 : values.length);
	}

	@Override
	public Object get(int index) {
		checkBounds(index);
		return values[index];
	}

	@Override
	public <T> void set(int index, T v) {
		checkBounds(index);
		this.values[index] = v;
	}

}
