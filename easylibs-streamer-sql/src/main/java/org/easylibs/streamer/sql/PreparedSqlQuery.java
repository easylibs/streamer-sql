package org.easylibs.streamer.sql;

import java.sql.PreparedStatement;

import org.easylibs.streamer.HasStream;
import org.easylibs.streamer.tuple.Tuple;

public interface PreparedSqlQuery<E> extends HasStream<E>, PreparedSql<PreparedSqlQuery<E>> {

	static PreparedSqlQuery<Tuple> of(PreparedStatement statement, PreparedSqlAction executeAction) {
		throw new UnsupportedOperationException();
	}

}
