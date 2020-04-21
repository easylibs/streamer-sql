package org.easylibs.streamer.sql.util;

import java.util.Spliterator;
import java.util.function.Consumer;

class DelegateSpliterator<T> implements Spliterator<T> {

	protected final Spliterator<T> parent;
	private final int characteristics;

	public DelegateSpliterator(int characteristics) {
		this.characteristics = characteristics;
		this.parent = null;
	}

	public DelegateSpliterator(Spliterator<T> parent) {
		this.parent = parent;
		this.characteristics = parent.characteristics();
	}

	public DelegateSpliterator(Spliterator<T> parent, int characteristics) {
		this.parent = parent;
		this.characteristics = characteristics;
	}

	@Override
	public int characteristics() {
		return characteristics;
	}

	@Override
	public long estimateSize() {
		return (parent == null) ? Long.MAX_VALUE : parent.estimateSize();
	}

	@Override
	public boolean tryAdvance(Consumer<? super T> action) {
		return (parent == null) ? false : parent.tryAdvance(action);
	}

	@Override
	public Spliterator<T> trySplit() {
		return (parent == null) ? null : parent.trySplit();
	}

}
