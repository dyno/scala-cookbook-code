// import $exec.SparkSessions

import $ivy.`org.apache.spark::spark-sql:3.1.1`
import $ivy.`sh.almond::ammonite-spark:0.10.1`

import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.encoders._
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.expressions._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

// https://stackoverflow.com/questions/53939687/scala-spark-org-apache-spark-repl-executorclassloader-failed-to-check-existe
implicit val spark: SparkSession = {
  val builder = AmmoniteSparkSession.builder
    .master("local[*]")
    .config("spark.driver.host", "127.0.0.1")
    .config("spark.home", sys.env("SPARK_HOME"))
    .config("spark.logConf", "true")

  if (sys.env.get("ENABLE_HIVE").contains("1")) {
    builder.enableHiveSupport()
  }

  builder.getOrCreate()
}

import org.apache.log4j.{Level, Logger}
val rootLogger = Logger.getRootLogger()
rootLogger.setLevel(Level.ERROR)
// rootLogger.setLevel(Level.WARN)

import spark.implicits._
