// https://medium.com/@mrpowers/manually-creating-spark-dataframes-b14dae906393
import $ivy.`com.github.mrpowers:spark-daria:v0.35.0`
interp.load.module(pwd / RelPath("libs/sparkSession.sc"))

{
// toDF()
import spark.implicits._

val someDF = Seq((8, "bat"), (64, "mouse"), (-27, "horse")).toDF("number", "word")
// number is not nullable
someDF.schema
}

{
  // createDataFrame()
  // full schema customization
  val someData = Seq(Row(8, "bat"), Row(64, "mouse"), Row(-27, "horse"))
  val someSchema = List(StructField("number", IntegerType, true), StructField("word", StringType, true))
  val someDF = spark.createDataFrame(spark.sparkContext.parallelize(someData), StructType(someSchema))
  someDF.show()
}

{ // createDF()
  // https://github.com/MrPowers/spark-daria/blob/master/src/main/scala/com/github/mrpowers/spark/daria/sql/SparkSessionExt.scala
  import com.github.mrpowers.spark.daria.sql.SparkSessionExt._
  val someDF =
    spark.createDF(List((8, "bat"), (64, "mouse"), (-27, "horse")), List(("number", IntegerType, true), ("word", StringType, true)))
}
