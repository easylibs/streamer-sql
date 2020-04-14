package org.easylibs.streamer.sql;

import org.easylibs.streamer.HasStream;
import org.easylibs.streamer.sql.SqlQuery.QueryBuilder;

public interface SqlJoin<E> extends HasStream<E> {

	public interface JoinBuilder<E> extends QueryBuilder<E> {

		JoinBuilder<E> innerJoinOn(String tableReference, String joinTableReference, String searchcondition);

		JoinBuilder<E> innerJoinUsing(String tableReference,
				String joinTableReference,
				String firstJoinColumn,
				String... moreJoinColumns);

		JoinBuilder<E> crossJoinUsing(String tableReference,
				String joinTableReference,
				String firstJoinColumn,
				String... moreJoinColumns);

		JoinBuilder<E> joinOn(String tableReference,
				String joinTableReference,
				String firstJoinColumn,
				String... moreJoinColumns);

	}

	PreparedSqlQuery<E> prepare();

}
