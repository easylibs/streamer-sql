package org.easylibs.streamer.sql.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

import org.easylibs.streamer.sql.PreparedSqlQuery;
import org.easylibs.streamer.sql.SqlStreamer;

public class StreamExample2 {

	public StreamExample2(SqlStreamer sql) throws SQLException {

		/* Regular SQL connection */
		Connection connection = sql.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Persons");

		PreparedSqlQuery.of(preparedStatement)
				.stream()
				.collect(Collectors.toList());
	}

}
