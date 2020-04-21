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
import java.util.Optional;
import java.util.function.Consumer;

import org.easylibs.streamer.builder.StreamerBuffer;
import org.easylibs.streamer.sql.SqlDelete.Builder;
import org.easylibs.streamer.sql.util.SqlUtils;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
class DeleteBuilderImpl extends SqlBuilder<DeleteBuilderImpl.Keyword, SqlDelete>
		implements SqlDelete.Builder {

	enum Keyword {
		DELETE_FROM,
		WHERE,
		ORDER_BY,
		LIMIT,
	}

	private final Connection connection;

	private Optional<Consumer<String>> sqlAction = Optional.empty();

	public DeleteBuilderImpl(Connection connection, String tableReference) {
		this.connection = connection;

		delete(tableReference);
	}

	private DeleteBuilderImpl delete(String tableReference) {
		set(Keyword.DELETE_FROM, tableReference);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.builder.StreamerBuilder#build()
	 */
	@Override
	public SqlDelete build() {

		final String sql = toSql();

		return new DeleteImpl(connection, sql);
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlBuilder#buildSql()
	 */
	@Override
	public String buildSql() {
		final StreamerBuffer body = new StreamerBuffer();
		final StreamerBuffer indented = body.newIndentedBuffer();

		body.setKeywordParser(SqlUtils::replaceUnderscores);
		body.enableFormatter(true);

		required(Keyword.DELETE_FROM)
				.header(body::sectionln)
				.forEach(indented::printd);

		optional(Keyword.WHERE)
				.header(body::sectionln)
				.forEach(indented::printd);

		optional(Keyword.ORDER_BY)
				.header(body::sectionln)
				.forEach(indented::printd);

		optional(Keyword.LIMIT)
				.header(body::sectionln)
				.forEach(indented::printd);

		final String sql = body.asString();

		sqlAction.ifPresent(action -> action.accept(sql));

		return sql;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlDelete.Builder#limit(long)
	 */
	@Override
	public Builder limit(long limit) {
		set(Keyword.LIMIT, Long.toString(limit));

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlDelete.Builder#limit(java.lang.String)
	 */
	@Override
	public Builder limit(String limit) {
		set(Keyword.LIMIT, limit);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlDelete.Builder#orderBy(java.lang.String)
	 */
	@Override
	public Builder orderBy(String orderBy) {
		set(Keyword.ORDER_BY, orderBy);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlDelete.Builder#where(java.lang.String)
	 */
	@Override
	public Builder where(String where) {
		set(Keyword.WHERE, where);

		return this;
	}

	/**
	 * @see org.easylibs.streamer.sql.SqlDelete.Builder#peekSql(java.util.function.Consumer)
	 */
	@Override
	public Builder peekSql(Consumer<String> action) {
		this.sqlAction = Optional.ofNullable(action);

		return this;
	}

}
