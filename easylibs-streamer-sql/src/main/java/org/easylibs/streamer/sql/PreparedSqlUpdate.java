package org.easylibs.streamer.sql;

public interface PreparedSqlUpdate extends PreparedSql<PreparedSqlUpdate> {

	int execute();
	
}
