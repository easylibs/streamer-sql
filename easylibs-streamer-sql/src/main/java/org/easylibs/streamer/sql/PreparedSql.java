package org.easylibs.streamer.sql;

public interface PreparedSql<P extends PreparedSql<P>> {

	P values(Object... values);

}
