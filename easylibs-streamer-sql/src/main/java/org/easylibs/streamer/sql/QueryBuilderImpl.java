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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.easylibs.streamer.builder.StreamerBuffer;
import org.easylibs.streamer.builder.StreamerException;
import org.easylibs.streamer.sql.SqlQuery.Builder;
import org.easylibs.streamer.sql.util.SqlUtils;
import org.easylibs.streamer.tuple.TupleX;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
class QueryBuilderImpl<E> extends SqlBuilder<QueryBuilderImpl.Keyword, SqlQuery<E>>
		implements Builder<E> {

	/**
	 * An SQL query is broken up into the following sections.
	 */
	enum Keyword {
		SELECT,
		DISTINCT,
		FROM,
		WHERE,
		HAVING,
		GROUP_BY,
		ORDER_BY,
		LIMIT,
		OFFSET,
	}

	Supplier<?> entitySupplier = TupleX::new;

	private final Connection connection;

	private Optional<Consumer<String>> sqlAction = Optional.empty();

	/**
	 * Instantiates a new query builder impl.
	 *
	 * @param connection the connection
	 */
	public QueryBuilderImpl(Connection connection) {
		this.connection = connection;
	}

	public QueryBuilderImpl(Connection connection, String tableReference) {
		this(connection, new String[] { tableReference });
	}

	public QueryBuilderImpl(Connection connection, String[] tableReferences) {
		this.connection = connection;

		from(tableReferences);
	}

	@Override
	public Builder<E> peekSql(Consumer<String> action) {
		this.sqlAction = Optional.ofNullable(action);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.StreamerSectionBuilder#build()
	 */
	@Override
	public SqlQuery<E> build() {
		final String sql = toSql();

		return new QueryImpl<>(connection, sql);
	}

	/**
	 * @see org.easylibs.streamer.StreamerSectionBuilder#buildSql()
	 */
	@Override
	public String buildSql() {

		final StreamerBuffer body = new StreamerBuffer();
		final StreamerBuffer indented = body.newIndentedBuffer();

		body.setKeywordParser(SqlUtils::replaceUnderscores);
		body.enableFormatter(true);

		required(Keyword.SELECT, Keyword.DISTINCT)
				.header(body::sectionln)
				.before(indented::setDelimiter, ", ")
				.forEach(indented::printd);

		required(Keyword.FROM)
				.header(body::sectionln)
				.before(indented::setDelimiter, ", ")
				.forEach(indented::printd);

		optional(Keyword.WHERE)
				.header(body::sectionln)
				.forEach(indented::printd);

		optional(Keyword.GROUP_BY)
				.header(body::sectionln)
				.before(indented::setDelimiter, ", ")
				.forEach(indented::printd);

		optional(Keyword.HAVING)
				.header(body::sectionln)
				.before(indented::setDelimiter, ", ")
				.forEach(indented::printd);

		optional(Keyword.ORDER_BY)
				.header(body::sectionln)
				.before(indented::setDelimiter, ", ")
				.forEach(indented::printd);

		optional(Keyword.LIMIT)
				.header(body::sectionln)
				.before(indented::setDelimiter, ", ")
				.forEach(indented::printd);

		optional(Keyword.OFFSET)
				.header(body::sectionln)
				.forEach(indented::printd);

		final String sql = body.asString();

		sqlAction.ifPresent(a -> a.accept(sql));

		return sql;
	}

	@Override
	public Builder<E> distinct() {
		set(Keyword.DISTINCT, Keyword.DISTINCT.name());

		return this;
	}

	Builder<E> from(String... tables) {
		Objects.requireNonNull(tables, "tables");
		if (tables.length > 0) {
			set(Keyword.FROM, tables);
		}

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQuery.Builder#groupBy(java.lang.String[])
	 */
	@Override
	public Builder<E> groupBy(String arg, String... args) {
		set(Keyword.GROUP_BY, arg, args);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQuery.Builder#having(java.lang.String)
	 */
	@Override
	public Builder<E> having(String expression) {
		set(Keyword.HAVING, expression);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQuery.Builder#limit(long)
	 */
	@Override
	public Builder<E> limit(long limit) {
		set(Keyword.LIMIT, Long.toString(limit));

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQuery.Builder#limit(long, long)
	 */
	@Override
	public Builder<E> limit(long offset, long limit) {
		set(Keyword.LIMIT, Long.toString(offset), Long.toString(limit));

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQuery.Builder#limit(java.lang.String)
	 */
	@Override
	public Builder<E> limit(String limit) {
		set(Keyword.LIMIT, limit);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQuery.Builder#limit(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Builder<E> limit(String offset, String limit) {
		if (contains(Keyword.OFFSET)) {
			throw new SqlBuilderException("duplicate offset defined");
		}

		set(Keyword.LIMIT, offset, limit);
		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQuery.Builder#offset(long)
	 */
	@Override
	public Builder<E> offset(long offset) {
		if (size(Keyword.LIMIT) == 2) {
			throw new SqlBuilderException("duplicate offset in limit defined");
		}

		set(Keyword.OFFSET, Long.toString(offset));

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQuery.Builder#offset(java.lang.String)
	 */
	@Override
	public Builder<E> offset(String offset) {
		set(Keyword.OFFSET, offset);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQuery.Builder#orderBy(java.lang.String[])
	 */
	@Override
	public Builder<E> orderBy(String arg, String... args) {
		set(Keyword.ORDER_BY, arg, args);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQuery.Builder#select(java.lang.Class,
	 *      java.lang.String[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <U> Builder<U> select(Class<U> type, String arg, String... args) {
		set(Keyword.SELECT, arg, args);

		entitySupplier = () -> {
			try {
				return type.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new StreamerException(e);
			}
		};

		return (Builder<U>) this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQuery.Builder#select(java.lang.String[])
	 */
	@Override
	public Builder<E> select(String arg, String... args) {
		set(Keyword.SELECT, arg, args);

		return this;
	}

	@Override
	public String toSql() {
		return buildSql();
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlQuery.Builder#where(java.lang.String)
	 */
	@Override
	public Builder<E> where(String expression) {
		set(Keyword.WHERE, expression);

		return this;
	}

}
