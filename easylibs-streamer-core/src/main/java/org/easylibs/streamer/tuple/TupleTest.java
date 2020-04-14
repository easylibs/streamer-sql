package org.easylibs.streamer.tuple;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TupleTest {

	@Test
	void tuple_assertThrows() {
		TupleVarargs tuple = new TupleVarargs();

		System.out.println(tuple);

		assertThrows(IndexOutOfBoundsException.class, () -> tuple.get(0));
		assertThrows(IndexOutOfBoundsException.class, () -> tuple.get(1));
	}

	@Test
	void tuple2_assertEquals() {
		Tuple2<?, ?> tuple = new Tuple2<>("Hello", 10);

		System.out.println(tuple);

		assertEquals("Hello", tuple.get(0, String.class));
		assertEquals(10, tuple.get(1, int.class));
	}

	@Test
	void tuple_assertEquals() {
		TupleVarargs tuple = new TupleVarargs("Hello", 10);

		System.out.println(tuple);

		assertEquals("Hello", tuple.get(0, String.class));
		assertEquals(10, tuple.get(1, int.class));
	}

}
