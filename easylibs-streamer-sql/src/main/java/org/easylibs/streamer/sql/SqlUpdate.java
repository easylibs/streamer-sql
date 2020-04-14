package org.easylibs.streamer.sql;

public interface SqlUpdate {

	public interface UpdateBuilder {

		SqlUpdate build();

		UpdateBuilder limit(long limit);

		UpdateBuilder limit(String limit);

		UpdateBuilder orderBy(String orderBy);

		UpdateBuilder set(String expr, String... args);

		UpdateBuilder where(String where);

	}

	PreparedSqlUpdate prepare();

	int execute();
}
