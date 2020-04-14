package org.easylibs.streamer.tuple;

public interface TupleGetter<T> {

	int degree();

	int index();

	T get();

	Class<T> type();
}
