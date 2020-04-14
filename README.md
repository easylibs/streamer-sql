# SQL Streamer - Ligthweight SQL Java Stream Adapter
Lightweight SQL to Java Stream API adaptor

## Examples
First we setup a `SqlStreamer`:
```java
final SqlStreamer streamer = SqlStreamer.of(ds.getConnection());
```
Next we show how to do the most basic SQL query and process the results using a Java `Stream`. This returns a single row, so only one line of output.

SQL:
```
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
```
SELECT AVG(score), t1.* FROM t1 ...;
```
Java:
```java
streamer.query("t1")
  .select("AVG(score)", "t1.*")
  .forEach(System.out::println);
```
