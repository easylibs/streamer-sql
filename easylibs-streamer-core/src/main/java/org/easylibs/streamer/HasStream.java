package org.easylibs.streamer;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface HasStream<E> {

	Stream<E> stream();

	default void forEach(Consumer<E> action) {
		stream().forEach(action);
	}

}
