package org.easylibs.streamer.sql;

public interface SqlInsert {

	public interface InsertBuilder {

		SqlInsert build();

		InsertBuilder into();

		InsertBuilder values(Object... values);

		InsertBuilder where(String where);

	}

	PreparedSqlUpdate prepare();

	int execute();
}
