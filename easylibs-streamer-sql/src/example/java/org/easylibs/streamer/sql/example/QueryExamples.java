package org.easylibs.streamer.sql.example;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.easylibs.streamer.sql.PreparedSqlQuery;
import org.easylibs.streamer.sql.SqlStreamer;
import org.easylibs.streamer.tuple.Tuple;

public class QueryExamples {

	public QueryExamples(DataSource ds) throws SQLException {

		final SqlStreamer streamer = SqlStreamer.of(ds.getConnection());

		/**
		 * <code>
		 * <pre>
		 *   sql> SELECT 1 + 1;
		 *     -> 2
		 * </pre>	
		 * </code>
		 */
		streamer.query()
				.select(int.class, "1 + 1")
				.forEach(System.out::println);

		/**
		 * <code>
		 * <pre>
		 *   sql> SELECT AVG(score), t1.* FROM t1 ...;
		 * </pre>	
		 * </code>
		 */
		streamer.query("t1")
				.select("AVG(score)", "t1.*")
				.forEach(System.out::println);

		/**
		 * <code>
		 * <pre>
		 *   sql> CONCAT(last_name,', ',first_name) AS full_name
		 * 	      FROM mytable ORDER BY full_name;
		 * </pre>	
		 * </code>
		 */
		streamer.query("mytable")
				.select(String.class, "CONCAT(last_name,', ',first_name) AS full_name")
				.orderBy("full_name")
				.forEach(System.out::println);

		/**
		 * <code>
		 * <pre>
		 *   sql> SELECT t1.name, t2.salary FROM employee AS t1, info AS t2
		 *        WHERE t1.name = t2.name;
		 * </pre>	
		 * </code>
		 */
		streamer.query("employee AS t1", "info AS t2")
				.select("t1.name", "t2.salary")
				.where("t1.name = t2.name")
				.forEach(System.out::println);

		/**
		 * <code>
		 * <pre>
		 *   sql> SELECT a, b, COUNT(c) AS t FROM test_table GROUP BY a,b ORDER BY a,t DESC;
		 * </pre>	
		 * </code>
		 */
		streamer.query("test_table")
				.select("a", "b", "COUNT(c)")
				.groupBy("a", "b")
				.orderBy("a", "t DESC")
				.forEach(System.out::println);

		/**
		 * <code>
		 * <pre>
		 *   sql> SELECT COUNT(col1) AS col2 FROM t GROUP BY col2 HAVING col2 = 2;
		 * </pre>	
		 * </code>
		 */
		streamer.query("t")
				.select("COUNT(col1) AS col2")
				.groupBy("col2")
				.having("col2 = 2")
				.forEach(System.out::println);

		/**
		 * <code>
		 * <pre>
		 *   sql> SELECT * FROM tbl LIMIT 5,10;  # Retrieve rows 6-15
		 * </pre>	
		 * </code>
		 */
		streamer.query("tbl")
				.limit(5, 10)
				.forEach(System.out::println);

		/**
		 * <code>
		 * <pre>
		 *   sql> SET @skip=1; SET @numrows=5;
		 *        PREPARE STMT FROM 'SELECT * FROM tbl LIMIT ?, ?';
		 *        EXECUTE STMT USING @skip, @numrows;
		 * </pre>	
		 * </code>
		 */
		PreparedSqlQuery<Tuple> prepared = streamer.queryBuilder("tbl")
				.limit("?", "?")
				.build()
				.prepare();

		prepared.values(1, 5)
				.stream()
				.forEach(System.out::println);

	}

}
