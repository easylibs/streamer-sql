package org.easylibs.streamer.sql.test;

import java.sql.SQLException;

import org.easylibs.streamer.sql.SqlDelete;
import org.easylibs.streamer.sql.SqlInsert;
import org.easylibs.streamer.sql.PreparedSqlUpdate;
import org.easylibs.streamer.sql.SqlStreamer;
import org.easylibs.streamer.sql.SqlUpdate;
import org.junit.jupiter.api.Test;

class UpdateTest {

	@Test
	void updateBuilder(SqlStreamer connection) throws SQLException {

		SqlUpdate update = connection.updateBuilder("t1")
				.set("col1 = col1 + 1", "col2 = col1")
				.orderBy("id DESC")
				.build();

		update.execute();
	}

	@Test
	void updateBuilder_prepare(SqlStreamer connection) throws SQLException {

		PreparedSqlUpdate prepared = connection.updateBuilder("items", "month")
				.set("col1 = ?", "col2 = ?")
				.build()
				.prepare();

		prepared.values(1, 2).execute();
	}

	@Test
	void insertBuilder(SqlStreamer connection) throws SQLException {

		SqlInsert update = connection.insertBuilder("items", "a", "b", "c")
				.values(1, 2, 3)
				.values(4, 5, 6)
				.values(7, 8, 9)
				.build();

		update.execute();
	}

	@Test
	void insertBuilder_prepare(SqlStreamer connection) throws SQLException {

		PreparedSqlUpdate prepared = connection.insertBuilder("items", "a", "b", "c")
				.values("?", "?", "?")
				.build()
				.prepare();

		prepared.values(1, 2, 3).execute();
		prepared.values(4, 5, 6).execute();
		prepared.values(7, 8, 9).execute();
	}

	@Test
	void deleteBuilder(SqlStreamer connection) throws SQLException {

		SqlDelete update = connection.deleteBuilder("items")
				.where("user = `jcole`")
				.orderBy("timestampColumn")
				.limit(1)
				.build();

		update.execute();
	}

	@Test
	void deleteBuilder_prepare(SqlStreamer connection) throws SQLException {

		PreparedSqlUpdate prepared = connection.deleteBuilder("items")
				.where("user = ?")
				.orderBy("timestampColumn")
				.values("jcole")
				.build()
				.prepare();

		prepared.execute();
	}

}
