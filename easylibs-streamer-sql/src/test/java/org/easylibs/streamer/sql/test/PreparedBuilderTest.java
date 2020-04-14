package org.easylibs.streamer.sql.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

import org.easylibs.streamer.sql.PreparedSqlQuery;
import org.easylibs.streamer.sql.SqlStreamer;
import org.easylibs.streamer.tuple.Tuple;
import org.junit.jupiter.api.Test;

class PreparedBuilderTest {

	@Test
	void preparedStatement(SqlStreamer sql) throws SQLException {
		/* Regular SQL connection */
		Connection connection = sql.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Persons");

		PreparedSqlQuery<Tuple> prepared = sql.using(preparedStatement, PreparedStatement::executeQuery);

		prepared.stream()
				.collect(Collectors.toList());
	}

	@Test
	void queryBuilder_streamPrepared(SqlStreamer sql) throws SQLException {

		PreparedSqlQuery<Tuple> prepared = sql.queryBuilder("Persons")
				.where("id > 100")
				.build()
				.prepare();

		prepared.stream()
				.collect(Collectors.toList());

		prepared.stream()
				.collect(Collectors.toList());

		prepared.forEach(System.out::println);
	}

}
