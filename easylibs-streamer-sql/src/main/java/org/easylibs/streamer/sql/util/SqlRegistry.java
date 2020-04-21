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
package org.easylibs.streamer.sql.util;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.easylibs.streamer.sql.function.SqlFunction;
import org.easylibs.streamer.sql.function.SqlSupplier;
import org.easylibs.streamer.tuple.Tuple;
import org.easylibs.streamer.tuple.Tuple1;
import org.easylibs.streamer.tuple.Tuple2;
import org.easylibs.streamer.tuple.Tuple3;
import org.easylibs.streamer.tuple.Tuple4;
import org.easylibs.streamer.tuple.Tuple5;
import org.easylibs.streamer.tuple.Tuple6;
import org.easylibs.streamer.tuple.Tuple7;
import org.easylibs.streamer.tuple.Tuple8;
import org.easylibs.streamer.tuple.Tuple9;
import org.easylibs.streamer.tuple.TupleX;

/**
 * Provides bindings for all SQL types and java primitives so that they can be
 * retrieved from a JDBC {@link ResultSet}.
 * 
 * <p>
 * To aquire a reference to the registery to register new bindings and types use
 * the {@link #global} method. All of the defaults are initialized from the
 * {@code static} initializer.
 * </p>
 * 
 * @author Sly Technologies
 * @author repos@slytechs.com
 */
public class SqlRegistry {

	private final static SqlRegistry global;

	public static SqlRegistry global() {
		return global;
	}

	static {
		global = new SqlRegistry();
		global.registerDefaults();
	}

	private final Map<Class<?>, SqlGetter<?>> javaTypes = new HashMap<>();
	private final Map<Integer, Class<?>> sqlTypes = new HashMap<>();

	/**
	 * 
	 */
	public SqlRegistry() {
		// TODO Auto-generated constructor stub
	}

	public <T> SqlGetter<T> get(Class<T> type) {
		return find(type)
				.orElseThrow(NoSuchElementException::new);
	}

	@SuppressWarnings("unchecked")
	public <T> SqlGetter<T> getSqlType(int type) {
		return (SqlGetter<T>) findSqlType(type)
				.orElseThrow(NoSuchElementException::new);
	}

	public <T> Optional<SqlGetter<T>> find(Class<T> type) {
		@SuppressWarnings("unchecked")
		SqlGetter<T> getter = (SqlGetter<T>) javaTypes.get(type);

		return Optional.ofNullable(getter);
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<SqlGetter<T>> findSqlType(int sqlType) {

		final Class<?> type = sqlTypes.get(sqlType);

		SqlGetter<T> getter = (SqlGetter<T>) get(type);

		return Optional.ofNullable(getter);
	}

	public <T> SqlGetter<T> register(Class<T> type, SqlGetter<T> getter) {
		javaTypes.put(type, getter);

		return getter;
	}

	public <T> SqlGetter<T> register(int sqlType, Class<T> registeredType) {

		@SuppressWarnings("unchecked")
		SqlGetter<T> getter = (SqlGetter<T>) javaTypes.get(registeredType);

		sqlTypes.put(sqlType, registeredType);

		return getter;
	}

	private void registerDefaults() {

		register(char.class, (i, rs) -> Character.valueOf((char) rs.getInt(i)));
		register(byte.class, (i, rs) -> rs.getByte(i));
		register(short.class, (i, rs) -> rs.getShort(i));
		register(int.class, (i, rs) -> rs.getInt(i));
		register(long.class, (i, rs) -> rs.getLong(i));
		register(float.class, (i, rs) -> rs.getFloat(i));
		register(double.class, (i, rs) -> rs.getDouble(i));
		register(boolean.class, (i, rs) -> rs.getBoolean(i));

		register(Character.class,
				(i, rs) -> rs.wasNull() ? null : Character.valueOf((char) rs.getInt(i)));
		register(Byte.class, (i, rs) -> rs.wasNull() ? null : rs.getByte(i));
		register(Short.class, (i, rs) -> rs.wasNull() ? null : rs.getShort(i));
		register(Integer.class, (i, rs) -> rs.wasNull() ? null : rs.getInt(i));
		register(Long.class, (i, rs) -> rs.wasNull() ? null : rs.getLong(i));
		register(Float.class, (i, rs) -> rs.wasNull() ? null : rs.getFloat(i));
		register(Double.class, (i, rs) -> rs.wasNull() ? null : rs.getDouble(i));
		register(Boolean.class, (i, rs) -> rs.wasNull() ? null : rs.getBoolean(i));
		register(BigDecimal.class, (i, rs) -> rs.wasNull() ? null : rs.getBigDecimal(i));

		register(Object.class, (i, rs) -> rs.wasNull() ? null : rs.getObject(i));
		register(Array.class, (i, rs) -> rs.wasNull() ? null : rs.getArray(i));
		register(byte[].class, (i, rs) -> rs.wasNull() ? null : rs.getBytes(i));

		register(Ref.class, (i, rs) -> rs.wasNull() ? null : rs.getRef(i));
		register(RowId.class, (i, rs) -> rs.wasNull() ? null : rs.getRowId(i));
		register(SQLXML.class, (i, rs) -> rs.wasNull() ? null : rs.getSQLXML(i));

		register(Statement.class, (i, rs) -> rs.wasNull() ? null : rs.getStatement());
		register(SQLWarning.class, (i, rs) -> rs.wasNull() ? null : rs.getWarnings());
		register(ResultSetMetaData.class, (i, rs) -> rs.wasNull() ? null : rs.getMetaData());

		register(Blob.class, (i, rs) -> rs.wasNull() ? null : rs.getBlob(i));
		register(Clob.class, (i, rs) -> rs.wasNull() ? null : rs.getClob(i));
		register(NClob.class, (i, rs) -> rs.wasNull() ? null : rs.getNClob(i));
		register(String.class, (i, rs) -> rs.wasNull() ? null : rs.getString(i));
		register(Date.class, (i, rs) -> rs.wasNull() ? null : rs.getDate(i));
		register(Time.class, (i, rs) -> rs.wasNull() ? null : rs.getTime(i));
		register(Timestamp.class, (i, rs) -> rs.wasNull() ? null : rs.getTimestamp(i));
		register(URL.class, (i, rs) -> rs.wasNull() ? null : rs.getURL(i));

		register(InputStream.class, (i, rs) -> rs.wasNull() ? null : rs.getBinaryStream(i));
		register(Reader.class, (i, rs) -> rs.wasNull() ? null : rs.getCharacterStream(i));

		register(Types.ARRAY, Array.class);
		register(Types.BLOB, Blob.class);
		register(Types.BOOLEAN, boolean.class);
		register(Types.CHAR, char.class);
		register(Types.CLOB, Clob.class);
		register(Types.DATE, Date.class);
		register(Types.DECIMAL, int.class);
		register(Types.DOUBLE, double.class);
		register(Types.FLOAT, float.class);
		register(Types.INTEGER, int.class);
		register(Types.LONGNVARCHAR, String.class);
		register(Types.LONGVARBINARY, byte[].class);
		register(Types.NCHAR, char.class);
		register(Types.NCLOB, NClob.class);
		register(Types.NUMERIC, int.class);
		register(Types.NVARCHAR, String.class);
		register(Types.REAL, float.class);
		register(Types.REF, Ref.class);
		register(Types.SMALLINT, short.class);
		register(Types.TIME, Time.class);
		register(Types.TIME_WITH_TIMEZONE, Time.class);
		register(Types.TIMESTAMP, Timestamp.class);
		register(Types.TIMESTAMP_WITH_TIMEZONE, Timestamp.class);
		register(Types.TINYINT, byte.class);
		register(Types.VARBINARY, byte[].class);
		register(Types.VARCHAR, String.class);

		registerTupleX(Tuple.class, TupleX::new);
		registerTupleX(TupleX.class, TupleX::new);
		registerTuple(Tuple1.class, Tuple1::new);
		registerTuple(Tuple2.class, Tuple2::new);
		registerTuple(Tuple3.class, Tuple3::new);
		registerTuple(Tuple4.class, Tuple4::new);
		registerTuple(Tuple5.class, Tuple5::new);
		registerTuple(Tuple6.class, Tuple6::new);
		registerTuple(Tuple7.class, Tuple7::new);
		registerTuple(Tuple8.class, Tuple8::new);
		registerTuple(Tuple9.class, Tuple9::new);
	}

	private <T extends Tuple> void registerTuple(Class<T> type, SqlSupplier<T> supplier) {
		/*
		 * These are most often used types, so we don't use lambas to save on execution
		 * and make debugging a little easier as well.
		 */
		register(type, new SqlGetter<T>() {

			@Override
			public T read(int colIndex, ResultSet rs) throws SQLException {
				return SqlUtils.readIntoTuple(rs, supplier.get());
			}
		});
	}

	/**
	 * TupleX allocation requires a 'degree' which they can get from resultset
	 * metadata.
	 *
	 * @param <T>      the generic type
	 * @param type     the type
	 * @param supplier the supplier
	 */
	private <T extends Tuple> void registerTupleX(Class<T> type, SqlFunction<Integer, T> supplier) {
		/*
		 * These are most often used types, so we don't use lambas to save on execution
		 * and make debugging a little easier as well. Further more, we ignore the
		 * supplier and allocate the TupleX directly.
		 */
		register(type, new SqlGetter<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T read(int colIndex, ResultSet rs) throws SQLException {
				return SqlUtils.readIntoTuple(rs, (T) new TupleX(rs.getMetaData().getColumnCount()));
			}
		});
	}
}
