package org.easylibs.streamer.tuple;

import java.util.Optional;
import java.util.stream.Stream;

public interface Tuple {

	int degree();

	Object get(int index);

	Optional<Object> getOptional(int index);

	<T> T get(int index, Class<T> type);

	<T> Optional<T> getOptional(int index, Class<T> type);

	<T> void set(int index, T v);

	Stream<?> stream();

	<T> Stream<T> streamOf(Class<T> type);

	default <T> TupleSetter<T> setter(int index, Class<T> type) {
		return Tuples.setter(this, index, type);
	}

	default <T> TupleGetter<T> getter(int index, Class<T> type) {
		return Tuples.getter(this, index, type);
	}
}