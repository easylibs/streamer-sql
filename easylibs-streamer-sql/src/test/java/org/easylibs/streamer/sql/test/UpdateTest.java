package org.easylibs.streamer.sql.test;

import java.sql.SQLException;

import org.easylibs.streamer.sql.PreparedSqlUpdate;
import org.easylibs.streamer.sql.SqlDelete;
import org.easylibs.streamer.sql.SqlInsert;
import org.easylibs.streamer.sql.SqlStreamer;
import org.easylibs.streamer.sql.SqlUpdate;
import org.junit.jupiter.api.Test;

class UpdateTest {

	@Test
	void updateBuilder(SqlStreamer connection) throws SQLException {

		SqlUpdate update = connection.update("t1")
				.set("col1 = col1 + 1", "col2 = col1")
				.orderBy("id DESC")
				.build();

		update.execute();
	}

	@Test
	void updateBuilder_prepare(SqlStreamer connection) throws SQLException {

		PreparedSqlUpdate prepared = connection.update("items")
				.set("col1 = ?", "col2 = ?")
				.build()
				.prepare();

		prepared.setNextValue(1).execute();
	}

	@Test
	void insertBuilder(SqlStreamer connection) throws SQLException {

		SqlInsert update = connection.insert("items")
				.columns("a", "b", "c")
				.repeat(1, 2, 3)
				.values(4, 5, 6)
				.values(7, 8, 9)
				.build();

		update.execute();
	}

	@Test
	void insertBuilder_prepare(SqlStreamer connection) throws SQLException {

		PreparedSqlUpdate prepared = connection.insert("items")
				.columns("a", "b", "c")
				.values("?", "?", "?")
				.build()
				.prepare(true);

		prepared.setNextValue(1).execute();
		prepared.setNextValue(4).execute();
		prepared.setNextValue(7).execute();
	}

	@Test
	void deleteBuilder(SqlStreamer connection) throws SQLException {

		SqlDelete update = connection.delete("items")
				.where("user = `jcole`")
				.orderBy("timestampColumn")
				.limit(1)
				.build();

		update.execute();
	}

	@Test
	void deleteBuilder_prepare(SqlStreamer connection) throws SQLException {

		PreparedSqlUpdate prepared = connection.delete("items")
				.where("user = ?")
				.orderBy("timestampColumn")
				.build()
				.prepare();

		prepared.setNextValue("Joe");
		prepared.execute();
	}

}
