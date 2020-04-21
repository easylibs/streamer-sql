/*
 * 
 */
package org.easylibs.streamer.sql.testutil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.easylibs.options.Arg;
import org.easylibs.options.Args;
import org.easylibs.options.InvalidArgException;
import org.easylibs.options.UnrecognizedArgException;
import org.easylibs.streamer.builder.StreamerException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@Arg
public class MysqlDatabaseTestbed {

	private static Logger log = Logger.getLogger(MysqlDatabaseTestbed.class.toString());

	public static Statement createStatement() throws SQLException {
		return of().connect().createStatement();
	}

	public static Connection getConnection() throws SQLException {
		return of().connect();
	}

	public static MysqlDatabaseTestbed of() {
		try {
			return new MysqlDatabaseTestbed();
		} catch (UnrecognizedArgException | InvalidArgException e) {
			throw new StreamerException(e);
		}
	}

	private static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";

	private String dbDriver = System.getProperty("db-driver", MYSQL_JDBC_DRIVER);
	private String dbServer = System.getProperty("db-server", "localhost");
	private String dbName = System.getenv("db-name");
	private String dbUser = System.getenv("db-user");
	private String dbPassword = System.getenv("db-password");

	public MysqlDatabaseTestbed() throws UnrecognizedArgException, InvalidArgException {
	}

	public MysqlDatabaseTestbed(final String[] args) throws UnrecognizedArgException, InvalidArgException {
		Args.of(args, this);
	}

	private Connection connect() throws SQLException {
		log.finest(dbName);
		return getDataSource().getConnection();
	}

	public DataSource getDataSource() {

		try {
			Class.forName(dbDriver);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();

			System.exit(1);
		}

		final MysqlDataSource ds = new MysqlDataSource();

		ds.setServerName(dbServer);
		ds.setUser(dbUser);
		ds.setPassword(dbPassword);
		ds.setDatabaseName(dbName);
		ds.setUseSSL(false);
		ds.setAutoReconnect(true);

		return ds;
	}

	@Override
	public String toString() {
		return "ExampleDataSourceFactory [dbServer=" + dbServer + ", dbName=" + dbName + ", user=" + dbUser
				+ ", password=" + dbPassword + "]";
	}

}
