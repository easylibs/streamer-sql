package org.easylibs.streamer.tuple;

import java.io.Serializable;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractTuple implements Serializable, Tuple {

	private static final long serialVersionUID = -3947048635987966734L;
	private final int degree;

	protected AbstractTuple(int degree) {
		this.degree = degree;
	}

	@Override
	public int degree() {
		return degree;
	}

	protected int checkBounds(int index) {
		if (index < 0 || index >= degree()) {
			throw new IndexOutOfBoundsException("Tuple index out of bounds " + index + " with degree " + degree());
		}

		return index;
	}

	protected Object getIndexed(int index, Object u) {
		if (index < 0 || index >= degree()) {
			throw new IndexOutOfBoundsException("Tuple index out of bounds " + index + " with degree " + degree());
		}

		return u;
	}

	@Override
	public abstract Object get(int index);

	@Override
	public Optional<Object> getOptional(int index) {
		return Optional.ofNullable(get(index));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(int index, Class<T> type) {
		return (T) get(index);
	}

	@Override
	public <T> Optional<T> getOptional(int index, Class<T> type) {
		return Optional.ofNullable(get(index, type));
	}

	@Override
	public abstract <T> void set(int index, T v);

	@Override
	public Stream<?> stream() {
		return StreamSupport.stream(new Spliterator<Object>() {

			@Override
			public int characteristics() {
				return 0;
			}

			@Override
			public long estimateSize() {
				return degree();
			}

			int i = 0;

			@Override
			public boolean tryAdvance(Consumer<? super Object> action) {
				
				if (i < degree()) {
					action.accept(get(i++));
				}

				return i < degree();
			}

			@Override
			public Spliterator<Object> trySplit() {
				return null;
			}

		}, false);
	}

	@Override
	public <T> Stream<T> streamOf(Class<T> type) {
		return stream()
				.filter(type::isInstance)
				.map(type::cast);
	}

	@Override
	public String toString() {
		final String name = getClass().getSimpleName();
		return name + " " + stream()
				.map(String::valueOf)
				.collect(Collectors.joining(", ", "[", "]"));
	}

}
