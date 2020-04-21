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

import java.sql.SQLException;

import org.easylibs.options.InvalidArgException;
import org.easylibs.options.UnrecognizedArgException;
import org.easylibs.streamer.tuple.Tuple;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Mark Bednarczyk [repo@slytechs.com]
 */
class SqlQueryBuilderTest {

	@Test
	void QueryExamples_1() throws SQLException, UnrecognizedArgException, InvalidArgException {

		QueryBuilderImpl<Tuple> builder = new QueryBuilderImpl<Tuple>(null, "`t1`");

		String sql = builder.select("1 + 1")
//				.distinct()
				.where("`col` < 20")
				.groupBy("`col1`")
				.having("`size` = 10")
				.orderBy("col1 ASC", "col2 DESC")
				.limit(300, 10)
//				.offset(200)
				.toSql();

//		assertEquals("SELECT 1 + 1", sql);

		System.out.println(sql);
	}

}
