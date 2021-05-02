val toHeightCm: String => Option[Double] = height => {
  val FOOT_CM = 30.48
  val INCH_CM = 2.54

  val heightCm: Double = height.split(raw"[^0-9]").filter(_ != "").map(_.toInt) match {
    case Array(cm, f) if cm > 100 => s"$cm.$f".toDouble
    case Array(f, i) => FOOT_CM * f + INCH_CM * i
    case Array(f) if f < 10 => FOOT_CM * f
    case Array(cm) => cm
    case _ => throw new NumberFormatException(height)
  }

  Some(heightCm).flatMap(h => if (h < 220.0) Some(h) else None)
}

val nullableToHeightCm: String => Option[Double] = height => {
  height match {
    case null => None
    case s: String => toHeightCm(s)
  }
}

// -------------------------------------------------------------------

import $exec.SparkSessions

val udfToHeightCm = udf(nullableToHeightCm)

val selected: Seq[Column] = Seq(
  col("Region"),
  col("ClassGrade").cast(IntegerType),
  col("Gender"),
  col("Handed"),
  udfToHeightCm(col("Height_cm")).alias("Height_cm"),
  trim(col("Languages_spoken")).cast(IntegerType).alias("Languages_spoken"),
  col("Birth_month"),
  col("Favorite_Season"),
  when(col("Allergies") === "Yes", true).when(col("Allergies") === "No", false).alias("Allergies"),
  when(col("Vegetarian") === "Yes", true).when(col("Vegetarian") === "No", false).alias("Vegetarian"),
  col("Favorite_Music"),
  col("Superpower"))

{
val census = spark.read
  .format("csv")
  .option("header", "true")
  .option("delimiter", ",")
  .option("inferSchema", "true")
  .load("1583456966205_DATA.csv")
}
val df = census.select(selected: _*)
df.show()

df.repartition(1).write.format("parquet").mode("overwrite").save("parquet")

spark.stop()
