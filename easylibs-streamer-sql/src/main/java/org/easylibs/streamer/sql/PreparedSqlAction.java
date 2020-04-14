package org.easylibs.streamer.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedSqlAction {

	void accept(PreparedStatement t) throws SQLException;
	
}
