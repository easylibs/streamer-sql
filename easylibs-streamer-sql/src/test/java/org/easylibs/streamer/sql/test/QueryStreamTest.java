package org.easylibs.streamer.sql.test;

import java.util.stream.Collectors;

import org.easylibs.streamer.sql.SqlStreamer;
import org.junit.jupiter.api.Test;

class QueryStreamTest {

	@Test
	void streamQuery(SqlStreamer streamer) {

		streamer.query("Persons")
				.where("id > 100")
				.collect(Collectors.toList());

	}

	@Test
	void test1(SqlStreamer streamer) {

		streamer.query("Persons")
				.where("id > 100")
				.collect(Collectors.toList());

	}

}
