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
package org.easylibs.streamer.sql.testutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.easylibs.streamer.sql.StreamerSqlException;
import org.easylibs.streamer.sql.function.SqlConsumer;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
public class DerbyDatabaseTestbed {

	private static Logger log = Logger.getLogger(DerbyDatabaseTestbed.class.toString());

	private final static Map<String, DerbyDatabaseTestbed> cache = new HashMap<>();

	public synchronized static Connection getCachedConnection(String dbName, SqlConsumer<Statement> schema)
			throws SQLException {

		DerbyDatabaseTestbed db = cache.get(dbName);
		if (db == null) {
			db = new DerbyDatabaseTestbed(dbName);

			schema.accept(db.connect().createStatement());
			cache.put(dbName, db);
		}

		return db.connect();
	}

	private final String protocol = "jdbc:derby:";
	private final String memory = "jdbc:derby:memory:";
	private final String connectUrl;
	private final String dropUrl;
	private final String shutdownUrl;
	private final String dbName;

	public DerbyDatabaseTestbed(String dbName) {
		this.dbName = dbName;
		connectUrl = memory + dbName + ";create=true";
		dropUrl = protocol + dbName + ";drop=true";
		shutdownUrl = protocol + "" + ";shutdown=true";
	}

	public void shutdown() {
		try {
			DriverManager.getConnection(shutdownUrl);
			log.finest(shutdownUrl);
		} catch (SQLException e) {
			if (!e.getSQLState().equals("XJ015")) {
				throw new StreamerSqlException(e);
			}
		}
	}

	public void drop() throws SQLException {
		DriverManager.getConnection(dropUrl);
		DerbyDatabaseTestbed.cache.remove(dbName);
		log.finest(dropUrl);
	}

	public Connection connect() throws SQLException {
		DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());

		log.finest(connectUrl);
		return DriverManager.getConnection(connectUrl);
	}

	public String toString() {
		return connectUrl;
	}

	public static void shutdownAll() {

		cache.entrySet()
				.stream()
				.map(e -> e.getValue())
				.forEach(DerbyDatabaseTestbed::shutdown);
	}

}
