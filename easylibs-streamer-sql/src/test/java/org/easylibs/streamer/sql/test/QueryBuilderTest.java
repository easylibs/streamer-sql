package org.easylibs.streamer.sql.test;

import java.sql.SQLException;
import java.util.stream.Collectors;

import org.easylibs.streamer.sql.SqlStreamer;
import org.easylibs.streamer.tuple.Tuple2;
import org.junit.jupiter.api.Test;

class QueryBuilderTest {

	@Test
	void joinBuilder(SqlStreamer streamer) throws SQLException {

	}

	@Test
	void queryBuilder_prepare(SqlStreamer streamer) throws SQLException {

		streamer.queryBuilder("Persons")
				.select(Tuple2.cast(int.class, String.class), "id, name")
				.where("id > 100")
				.build()
				.prepare()
				.stream()
				.collect(Collectors.toList());

	}

}
