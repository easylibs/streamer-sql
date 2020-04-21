/*
 * MIT License
 * 
 * Copyright (c) 2020 Sly Technologies Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.easylibs.streamer.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

import org.easylibs.streamer.sql.function.SqlConsumer;
import org.easylibs.streamer.sql.testutil.DerbyDatabaseTestbed;
import org.easylibs.streamer.sql.util.ResultSetSpliterator;
import org.easylibs.streamer.tuple.Tuple;
import org.easylibs.streamer.tuple.Tuple1;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
@SuppressWarnings("resource")
public class SqlQueryDerbyTest {

	private final String dbName = "SqlQueryTest";

	private final String dummy = "dummy";
	private final String db1 = "db1";
	private final String t1 = "t1";
	private final String employee = "employee";
	private final String info = "info";
	private final String mytable = "mytable";
	private final String testTable = "test_table";

	private void dbSchema(Statement s) throws SQLException {

		s.executeUpdate("CREATE TABLE " + dummy + " (nothing INT)");
		s.executeUpdate("CREATE TABLE " + db1 + " (col1 INT, col2 VARCHAR(64))");
		s.executeUpdate("CREATE TABLE " + t1 + " (score INT)");
		s.executeUpdate("CREATE TABLE " + employee + " (name VARCHAR(64))");
		s.executeUpdate("CREATE TABLE " + info + " (name VARCHAR(64), salary INT)");
		s.executeUpdate("CREATE TABLE " + mytable + " (first_name VARCHAR(64), last_name VARCHAR(64))");
		s.executeUpdate("CREATE TABLE " + testTable + " (a INT, b INT, c INT, t INT)");

		dbPopulate(s);
	}

	@AfterAll
	public static void shutdown() {
		DerbyDatabaseTestbed.shutdownAll();
	}

	private void dbPopulate(Statement s) throws SQLException {

		s.executeUpdate("INSERT INTO " + t1 + "(score) VALUES (1), (2), (3), (4), (5)");

		s.executeUpdate("INSERT INTO " + db1 + "(col1,col2) VALUES (1,'a'), (2,'b'), (3,'c'), (4,'d'), (5,'e')");

		s.executeUpdate("INSERT INTO " + mytable + "(first_name, last_name) VALUES"
				+ "('bart', 'b'), ('alice', 'a'), ('cerci', 'c')");

		s.executeUpdate("INSERT INTO " + employee + "(name) VALUES"
				+ "('bart'), ('alice'), ('cerci')");

		s.executeUpdate("INSERT INTO " + info + "(name, salary) VALUES"
				+ "('bart', 50000), ('alice', 60000), ('cerci', 70000)");

		s.executeUpdate("INSERT INTO " + testTable + "(a,b,c) VALUES (2, 20, 200), (1, 10, 100), (3, 30, 300)");

	}

	private ResultSet executeQuery(String sql) throws SQLException {
		return executeQuery(sql, dbName, this::dbSchema);
	}

	private ResultSet executeQuery(String sql, String dbName, SqlConsumer<Statement> schema) throws SQLException {
		final Connection conn = DerbyDatabaseTestbed.getCachedConnection(dbName, this::dbSchema);

		return conn.createStatement().executeQuery(sql);
	}

//	@Test
	void QueryExamples_1() throws SQLException {
		QueryBuilderImpl<Tuple> builder = new QueryBuilderImpl<Tuple>(null);

		builder.from(dummy)
				.select("1 + 1");

		System.out.println("QueryExamples_1: " + builder.toSql());

		final String sql = builder.toSql();
		ResultSet rs = executeQuery(sql);

		Spliterator<Tuple> spliterator = new ResultSetSpliterator<>(rs);
		StreamSupport.stream(spliterator, false)
				.forEach(System.out::println);
	}

//	@Test
	void QueryExamples_2() throws SQLException {
		QueryBuilderImpl<Tuple> builder = new QueryBuilderImpl<Tuple>(null);

		builder.from(t1)
				.select("AVG(t1.score)");

		System.out.println("QueryExamples_2: " + builder.toSql());

		final String sql = builder.toSql();
		ResultSet rs = executeQuery(sql);

		Spliterator<Tuple> spliterator = new ResultSetSpliterator<>(rs);
		StreamSupport.stream(spliterator, false)
				.forEach(System.out::println);
	}

//	@Test
	void QueryExamples_3() throws SQLException {
		QueryBuilderImpl<Tuple> builder = new QueryBuilderImpl<Tuple>(null);

		builder.from(mytable)
				.select("last_name || ', ' || first_name AS full_name")
//				.orderBy("full_name")
		;

		System.out.println("QueryExamples_3: " + builder.toSql());

		final String sql = builder.toSql();
		ResultSet rs = executeQuery(sql);

//		ResultSetStream.of(rs)
//				.forEach(System.out::println);

		Spliterator<Tuple> spliterator = new ResultSetSpliterator<>(
				rs,
				(i, r) -> new Tuple1<String>(rs.getString(1))
						.setLabel(0, r.getMetaData().getColumnLabel(1)));

		StreamSupport.stream(spliterator, false)
				.filter(a -> true)
				.forEach(System.out::println);
	}

//	@Test
	void QueryExamples_4() throws SQLException {
		QueryBuilderImpl<Tuple> builder = new QueryBuilderImpl<Tuple>(null);

		builder.from(employee + " AS t1", info + " AS t2")
				.select("t1.name", "t2.salary")
				.where("t1.name = t2.name");

		System.out.println("QueryExamples_4: " + builder.toSql());

		final String sql = builder.toSql();
		ResultSet rs = executeQuery(sql);

		Spliterator<Tuple> spliterator = new ResultSetSpliterator<>(rs);
		StreamSupport.stream(spliterator, false)
				.map(tuple -> String.format("%10s = $%,d", tuple.get(0, String.class), tuple.get(1, int.class)))
				.forEach(System.out::println);
	}

//	@Test
	void QueryExamples_5() throws SQLException {
		QueryBuilderImpl<Tuple> builder = new QueryBuilderImpl<Tuple>(null);

		builder.from(testTable)
				.select("a", "b", "t")
//				.groupBy("a")
				.orderBy("a", "t");

		System.out.println("QueryExamples_5: " + builder.toSql());

		final String sql = builder.toSql();
		ResultSet rs = executeQuery(sql);

		try {
			Spliterator<Tuple> spliterator = new ResultSetSpliterator<>(rs);
			StreamSupport.stream(spliterator, false)
					.forEach(System.out::println);
		} catch (StreamerSqlException e) {
			throw e.getCause();
		}
	}

//	@Test
	void QueryExamples_6() throws SQLException {
		final Connection connection = DerbyDatabaseTestbed.getCachedConnection(dbName, this::dbSchema);

		SqlStreamerSupport.streamer(connection)
				.query(testTable)
				.select(int.class, "a")
				.where("a = 1 OR B > 20 AND b < 40")
				.peekSql(System.out::println)
				.peekSql(sql -> System.out.println("\nResults:"))
				.forEach(a -> System.out.println("a=" + a))

		;
	}

	@Test
	void QueryExamples_7_() throws SQLException {
		final Connection connection = DerbyDatabaseTestbed.getCachedConnection(dbName, this::dbSchema);

		System.out.println("Connection.catalog:");
		SqlStreamerSupport.stream(connection.getMetaData().getCatalogs(), String.class)
				.forEach(System.out::println);

		for (Tuple i : SqlStreamerSupport.iterable(connection.getMetaData().getCatalogs())) {
			System.out.println(i);
		}

		SqlStreamerSupport.streamer(connection)
				.query(db1)
				.select("col1", "col2")
				.where("col1 > 3")
				.peekSql(System.out::println)
				.map(tuple -> "Data: " + tuple.get(0) + " - " + tuple.get(1))
				.forEach(System.out::println);

	}

}
