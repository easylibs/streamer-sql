# SQL Streamer - Ligthweight SQL Java Stream Adapter
Lightweight SQL to Java Stream API adaptor

## Examples
First we setup a `SqlStreamer`:
```java
1| final java.sql.DataSource ds = ...; // Assume injected in someway
2| final SqlStreamer streamer = SqlStreamer.of(ds.getConnection());
```
This first example shows, how to do a basic SQL query and process the results using a Java `Stream`. This example returns a single row, so only one line of output is produced, but of course as a stream its setup to handle any number of results.

SQL:
```sql
1| SELECT 1 + 1;
```

Java:
```java
1| streamer.query()
2|         .select(int.class, "1 + 1") 
3|         .forEach(System.out::println);
```

Output:
```
2
```

| Line # | Description                                                                                            |
|:------:|--------------------------------------------------------------------------------------------------------|
| 1      | creates a query backed by a `java.sql.Statement`|
| 2      | adds a `select 1 + 1` clause to the query and `int.class` maps the `SelectStream` to an `Integer` type |
| 3      | is `Stream::forEach(Consumer<Integer> action)` which gets called with each row of the `ResultSet`|

Using Tuples
---
This query selects all columns in table `t1` and also executes an SQL `AVG()` function to average the `score`. By default
if you do not map the `select` statement result as something else, the default will be a `Tuple` which can take any number
of arbitrary arguments. Also specialized versions of tuples exist for different number of parameters such as `Tuple1`, `Tuple9`, etc..

SQL:
```sql
1| SELECT AVG(score), t1.* FROM t1 ...;
```

Java:
```java
1| streamer.query("t1")
2|         .select("AVG(score)", "t1.*")
3|         .forEach(System.out::println);
```
Prepared Query
---

Lastly here is an example that shows how use a `PreparedStatement` backed query. The prepared query can be reused over and over by supplying different values 

SQL:
```sql
1| SET @skip=1; SET @numrows=5;
2| PREPARE STMT FROM 'SELECT * FROM tbl LIMIT ?, ?';
3| EXECUTE STMT USING @skip, @numrows;
```

Java:
```java
1| PreparedSqlQuery<Tuple> prepared = streamer.queryBuilder("tbl")
2|         .limit("?", "?")
3|         .build()    // Build a query statement
4|         .prepare(); // Impl: Make it a java.sql.PreparedStatement
```
---

and now we use the prepared query by supplying the values with `values(1, 5)` to replace all of the `?` in the query. When the `.stream()` is called, the query is executed and the `Stream` returned will iterate over the returned `ResultSet`. 

**Note** that `PrearedSqlQuery::stream` does not return the enhanced `QueryStream`, but the normal `java.util.stream.Stream` which does not allow the prepared query to be further customized.


```java
1| prepared.values(1, 5) // Substitute values for '?'
2|         .stream()     // Execute query and stream over results
3|         .forEach(System.out::println);
```

For more example please check the [examples directory](https://github.com/easylibs/streamer-sql/tree/master/easylibs-streamer-sql/src/example/java/org/easylibs/streamer/sql/example) in the repository.

