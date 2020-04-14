# SQL Streamer - Ligthweight SQL Java Stream Adapter
Lightweight SQL to Java Stream API adaptor

## Examples
First we setup a `SqlStreamer`:
```java
final SqlStreamer streamer = SqlStreamer.of(ds.getConnection());
```
Next example shows, how to do a basic SQL query and process the results using a Java `Stream`. This example returns a single row, so only one line of output is produced, but of course as a stream its setup to handle any number of results.

SQL:
```sql
SELECT 1 + 1;
```
Java:
```java
streamer.query()
 .select(int.class, "1 + 1")
 .forEach(System.out::println);
```
Output:
```
2
```

This query selects all columns in table `t1` and also executes an SQL `AVG()` function to average the `score`:
SQL:
```sql
SELECT AVG(score), t1.* FROM t1 ...;
```
Java:
```java
streamer.query("t1")
  .select("AVG(score)", "t1.*")
  .forEach(System.out::println);
```
Lastly here is an example that shows how use a `PreparedStatement` query. The prepared statement can be reused over and over:
SQL:
```sql
SET @skip=1; SET @numrows=5;
PREPARE STMT FROM 'SELECT * FROM tbl LIMIT ?, ?';
EXECUTE STMT USING @skip, @numrows;
```
Java:
```java
PreparedSqlQuery<Tuple> prepared = streamer.queryBuilder("tbl")
  .limit("?", "?")
  .build()
  .prepare();
```
and now we use the prepared statement, supplying the values with `values(1, 5)` to replace all of the `?` in the query:
```java
prepared.values(1, 5)
  .stream()
  .forEach(System.out::println);
```

For more example please check the Wiki pages.
