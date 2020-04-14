package org.easylibs.streamer.tuple;

public interface TupleSetter<T> {

	int degree();

	int index();

	void set(T value);

	Class<T> type();
}
