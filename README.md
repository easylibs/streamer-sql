# SQL Streamer - Ligthweight SQL Java Stream Adapter
Lightweight SQL to Java Stream API adaptor

## Examples
First we setup a `SqlStreamer`:
```java
final java.sql.DataSource ds = ...; // Assume injected in someway
final SqlStreamer streamer = SqlStreamer.of(ds.getConnection());
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
The example above
This query selects all columns in table `t1` and also executes an SQL `AVG()` function to average the `score`:

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
Lastly here is an example that shows how use a `PreparedStatement` query. The prepared statement can be reused over and over:

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

and now we use the prepared statement, supplying the values with `values(1, 5)` to replace all of the `?` in the query:


```java
1| prepared.values(1, 5) // Substitute values for '?'
2|         .stream()     // Execute query and stream over results
3|         .forEach(System.out::println);
```

For more example please check the Wiki pages.
