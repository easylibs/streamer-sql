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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.easylibs.streamer.sql.testutil.DerbyDatabaseTestbed;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
public class SqlInsertDerbyTest {

	private final String dbName = "SqlQueryTest";

	private final String dummy = "dummy";
	private final String t1 = "t1";
	private final String employee = "employee";
	private final String info = "info";
	private final String mytable = "mytable";
	private final String testTable = "test_table";
	private final String greetings = "greetings";

	private void dbSchema(Statement s) throws SQLException {

		s.executeUpdate("CREATE TABLE " + dummy + " (nothing INT)");
		s.executeUpdate("CREATE TABLE " + t1 + " (score INT)");
		s.executeUpdate("CREATE TABLE " + employee + " (name VARCHAR(64))");
		s.executeUpdate("CREATE TABLE " + info + " (name VARCHAR(64), salary INT)");
		s.executeUpdate("CREATE TABLE " + mytable + " (first_name VARCHAR(64), last_name VARCHAR(64))");
		s.executeUpdate("CREATE TABLE " + testTable + " (a INT, b INT, c INT, t INT)");
		s.executeUpdate(
				"CREATE TABLE " + greetings + " (id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, msg VARCHAR(64))");

		dbPopulate(s);
	}

	@AfterAll
	public static void shutdown() {
		DerbyDatabaseTestbed.shutdownAll();
	}

	private void dbPopulate(Statement s) throws SQLException {

		s.executeUpdate("INSERT INTO " + t1 + "(score) VALUES (1), (2), (3), (4), (5)");

		s.executeUpdate("INSERT INTO " + mytable + "(first_name, last_name) VALUES"
				+ "('bart', 'b'), ('alice', 'a'), ('cerci', 'c')");

		s.executeUpdate("INSERT INTO " + employee + "(name) VALUES"
				+ "('bart'), ('alice'), ('cerci')");

		s.executeUpdate("INSERT INTO " + info + "(name, salary) VALUES"
				+ "('bart', 50000), ('alice', 60000), ('cerci', 70000)");

		s.executeUpdate("INSERT INTO " + testTable + "(a,b,c) VALUES (2, 20, 200), (1, 10, 100), (3, 30, 300)");

	}

	private Connection getConnection() throws SQLException {
		final Connection conn = DerbyDatabaseTestbed.getCachedConnection(dbName, this::dbSchema);

		return conn;
	}

//	@Test
	public void InsertExamples_1() throws SQLException {
		SqlStreamer streamer = SqlStreamerSupport.streamer(getConnection());

		int result = streamer.insert(t1)
				.columns("score")
				.values(20)
				.peekSql(System.out::println)
				.execute();

		System.out.println("result=" + result);

		streamer.query("t1")
				.forEach(System.out::println);
	}

	/**
	 * Example creates a prepared update for an insert. Value are applied a single
	 * row at a time and the auto generated key is displayed.
	 * 
	 * DERBY only returns a single key.
	 * 
	 * From Oracle docs:
	 * 
	 * https://docs.oracle.com/javadb/10.10.1.2/ref/crefjavstateautogen.html
	 * 
	 * The Derby implementation of Statement.getGeneratedKeys returns meaningful
	 * results only if the last statement was a single-row insert statement. If it
	 * was a multi-row insert, Statement.getGeneratedKeys will return a result set
	 * with only a single row, even though it should return one row for each
	 * inserted row.
	 */
//	@Test
	public void InsertExamples_one_at_a_time() throws SQLException {
		SqlStreamer streamer = SqlStreamerSupport.streamer(getConnection());

		PreparedSqlUpdate insert = streamer.insert(greetings)
				.columns("msg")
				.values("?")
				.prepare(true);

		System.out.println(insert.toSql());

		Stream.of("Hello", "There", "World", "how", "is", "everyone", "doing")
				.flatMap(insert::setValueAndExecute)
				.forEach(id -> System.out.println("Auto generated key: " + id));

		streamer.query(greetings)
				.forEach(System.out::println);
	}

	@Test
	public void InsertExamples_multiple_at_a_time() throws SQLException {
		SqlStreamer streamer = SqlStreamerSupport.streamer(getConnection());

		List<String> data = Arrays.asList("Hello", "There", "World", "how", "is", "everyone", "doing");

		PreparedSqlUpdate insert = streamer.insert(greetings)
				.columns("msg")
				.repeat(data.size(), "?")
				.prepare(true);

		data.forEach(insert::setNextValue);

		System.out.println(insert.toSql());

		insert.execute();

		streamer.query(greetings)
				.forEach(System.out::println);
	}

}
