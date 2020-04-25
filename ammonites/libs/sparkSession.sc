import $exec.`compatPost`

// https://github.com/alexarchambault/ammonite-spark#compatibility
log(interp.load.ivy(AmmSpark.depOfAmmoniteSpark))
@

val sparkSessionModule = sys.env.get("USE_AMMONITE_SPARK") match {
  case Some("1") => pwd / RelPath("libs/_AmmoniteSparkSession.sc")
  case _ => pwd / RelPath("libs/_SparkSession.sc")
}
interp.load.module(sparkSessionModule)
@

import org.apache.spark.sql._
import org.apache.spark.sql.catalyst.encoders._
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.expressions._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

// https://stackoverflow.com/questions/53939687/scala-spark-org-apache-spark-repl-executorclassloader-failed-to-check-existe
implicit val spark: SparkSession = {
  val builder = AmmSparkSession.builder
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
