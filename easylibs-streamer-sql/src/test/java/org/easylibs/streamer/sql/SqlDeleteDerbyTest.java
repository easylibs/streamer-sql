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

import org.easylibs.streamer.sql.testutil.DerbyDatabaseTestbed;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
public class SqlDeleteDerbyTest {

	private final String dbName = "SqlQueryTest";

	private final String dummy = "dummy";
	private final String table1 = "t1";
	private final String employee = "employee";
	private final String info = "info";
	private final String mytable = "mytable";
	private final String testTable = "test_table";
	private final String greetings = "greetings";

	private void dbSchema(Statement s) throws SQLException {

		s.executeUpdate("CREATE TABLE " + dummy + " (nothing INT)");
		s.executeUpdate("CREATE TABLE " + table1 + " (score INT)");
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

		s.executeUpdate("INSERT INTO " + table1 + "(score) VALUES (1), (2), (3), (4), (5)");

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
	public void DeleteExamples_1() throws SQLException {
		SqlStreamer streamer = SqlStreamerSupport.streamer(getConnection());

		int result = streamer.delete(table1)
				.where("score > 3")
				.peekSql(System.out::println)
				.execute();

		System.out.println("Deleted count=" + result);

		streamer.query(table1)
				.forEach(System.out::println);
	}

	@Test
	public void DeleteExamples_2() throws SQLException {
		SqlStreamer streamer = SqlStreamerSupport.streamer(getConnection());

		PreparedSqlUpdate delete = streamer.delete(table1)
				.where("score > ?")
				.prepare();

		delete.setNextValue(3);

		System.out.println(delete.toSql());

		int result = delete.execute();
		System.out.println("Deleted count=" + result);

		streamer.query(table1)
				.forEach(System.out::println);
	}

}
