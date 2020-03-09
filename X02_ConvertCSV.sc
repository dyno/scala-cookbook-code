interp.load.module(pwd / RelPath("libs/sparkSession.sc"))
import spark.implicits._

import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

val df = Seq(("11", "true"), ("22", "false"), ("33", "?"), ("44", "")).toDF("id", "vegetarian")

val converted = df.select(
  col("id").cast(IntegerType).alias("id"),
  when(col("vegetarian") === "?", null)
    .when(col("vegetarian") === "", null)
    .otherwise(col("vegetarian").cast(BooleanType))
    .alias("vegetarian"))

converted.queryExecution
converted.schema

converted.show()
