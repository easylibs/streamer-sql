package org.easylibs.streamer.sql;

public interface SqlDelete {

	public interface DeleteBuilder {

		SqlDelete build();

		DeleteBuilder limit(long limit);

		DeleteBuilder limit(String limit);

		DeleteBuilder orderBy(String orderBy);

		DeleteBuilder values(Object... values);

		DeleteBuilder where(String where);
	}

	PreparedSqlUpdate prepare();

	int execute();
}
