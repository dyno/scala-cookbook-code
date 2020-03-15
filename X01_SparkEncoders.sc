interp.load.module(pwd / RelPath("libs/sparkSession.sc"))

import org.apache.log4j.{Level, Logger}
val rootLogger = Logger.getRootLogger()
rootLogger.setLevel(Level.ERROR)


import spark.implicits._

show(repl.fullImports)
repl.imports.value.collect({ case imp: ImportData if imp.prefix.map(_.raw).mkString(".").contains("spark.implicits") => imp.fromName.raw })


{
val df = Seq("what", "ever").toDF("foo")
df.schema
// desugar(Seq("what", "ever").toDF("foo"))
case class Foo(foo: String)
df.as[Foo].collect().foreach(println)
// desugar(df.as[Foo])
}

// https://stackoverflow.com/questions/36648128/how-to-store-custom-objects-in-dataset
{
// Just use kryo
//
// XXX: does not work in ammonite repl at all ...
// XXX: Accessing encoders for classes defined inside the ammonite session
// https://github.com/alexarchambault/ammonite-spark/issues/19
// https://stackoverflow.com/questions/40229953/spark-dataset-example-unable-to-generate-an-encoder-issue

org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this); class MyObj(val i: Int)
implicit val myObjEncoder = org.apache.spark.sql.Encoders.kryo[MyObj]

val d1 = spark.createDataset(Seq(new MyObj(1), new MyObj(2), new MyObj(3)))
val d2 = d1.map(d => (d.i + 1, d)).alias("d2") // mapping works fine and ..
val d3 = d1.map(d => (d.i, d)).alias("d3") // .. deals with the new type
val d4 = d2.joinWith(d3, $"d2._1" === $"d3._1") // Boom!
}

{
// Partial solution for tuples
//
// implicit + tuple + kryo

import org.apache.spark.sql.{Encoder, Encoders}
import scala.reflect.ClassTag
import spark.implicits._ // we can still take advantage of all the old implicits

implicit def single[A](implicit c: ClassTag[A]): Encoder[A] = Encoders.kryo[A](c)
implicit def tuple2[A1, A2](implicit e1: Encoder[A1], e2: Encoder[A2]): Encoder[(A1, A2)] = Encoders.tuple[A1, A2](e1, e2)
implicit def tuple3[A1, A2, A3](implicit e1: Encoder[A1], e2: Encoder[A2], e3: Encoder[A3]): Encoder[(A1, A2, A3)] =
  Encoders.tuple[A1, A2, A3](e1, e2, e3)

class MyObj(val i: Int)
val d1 = spark.createDataset(Seq(new MyObj(1), new MyObj(2), new MyObj(3)))
val d2 = d1.map(d => (d.i + 1, d)).toDF("_1", "_2").as[(Int, MyObj)].alias("d2")
val d3 = d1.map(d => (d.i, d)).toDF("_1", "_2").as[(Int, MyObj)].alias("d3")
val d4 = d2.joinWith(d3, $"d2._1" === $"d3._1")
}

{
// Partial solution for classes in general

class MyObj(val i: Int, val u: java.util.UUID, val s: Set[String])
// alias for the type to convert to and from
type MyObjEncoded = (Int, String, Set[String])

// implicit conversions
implicit def toEncoded(o: MyObj): MyObjEncoded = (o.i, o.u.toString, o.s)
implicit def fromEncoded(e: MyObjEncoded): MyObj = new MyObj(e._1, java.util.UUID.fromString(e._2), e._3)

val d = spark
  .createDataset(
    Seq[MyObjEncoded](new MyObj(1, java.util.UUID.randomUUID, Set("foo")), new MyObj(2, java.util.UUID.randomUUID, Set("bar"))))
  .toDF("i", "u", "s")
  .as[MyObjEncoded]

d.show()
}

{
// https://stackoverflow.com/questions/31477598/how-to-create-an-empty-dataframe-with-a-specified-schema
case class Student(age:String, id:String, name:String)

import org.apache.spark.sql.catalyst.ScalaReflection
val studentSchema = ScalaReflection.schemaFor[Student].dataType.asInstanceOf[StructType]

val studentSchema = Encoders.product[Student].schema

val studentSchema = StructType(
  Seq(StructField("age", StringType, true), StructField("id", StringType, true), StructField("name", StringType, true)))

// https://jaceklaskowski.gitbooks.io/mastering-spark-sql/spark-sql-ExpressionEncoder.html
// > resolveAndBind is used when:
// > ...
// > Dataset is requested for the deserializer expression (to convert internal rows to objects of type T)
val schema = StructType(Seq(StructField("students", ArrayType(studentSchema), true), StructField("age", StringType, true)))
val encoder = RowEncoder(schema).resolveAndBind()

// https://github.com/apache/spark/blob/master/sql/catalyst/src/test/scala/org/apache/spark/sql/catalyst/encoders/RowEncoderSuite.scala
val row = Row(Seq(Row("11", "111", "eleven"), Row("11", "112", "eleven-2")), "11")
val internalRow = encoder.toRow(row)
val row = encoder.fromRow(internalRow)


// https://stackoverflow.com/questions/46525530/spark-how-to-combine-merge-elements-in-dataframe-which-are-in-seqrow-to-gene

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions._

val jsonData = """
{"id" : "1201", "name" : "satish", "age" : "25"}
{"id" : "1202", "name" : "krishna", "age" : "28"}
{"id" : "1203", "name" : "amith", "age" : "28"}
{"id" : "1204", "name" : "javed", "age" : "23"}
{"id" : "1205", "name" : "mendy", "age" : "25"}
{"id" : "1206", "name" : "rob", "age" : "24"}
{"id" : "1207", "name" : "prudvi", "age" : "23"}
""".split("\n").toSeq

val df = spark.read.json(jsonData.toDS)
df.show()

val windowSpec = Window.partitionBy("age").orderBy("id")
val mergedDF = df.withColumn("students", collect_set(struct("age", "id", "name")).over(windowSpec)).select("students", "age")
mergedDF.show(false)

val arrLen = udf { a: Seq[Row] => a.length > 1 }
val filtered = mergedDF.filter(arrLen(col("students")))
filtered.show(false)
filtered.cache()

// https://stackoverflow.com/questions/37553059/extracting-seqstring-string-string-from-spark-dataframe
val df = filtered
df.map(row => row.getAs[String]("age")).collect()

// Work with case class
case class Student(age:String, id:String, name:String)
case class AgeStudents(students:Seq[Student], age:String)
val ds = df.as[AgeStudents]
ds.map(_.students).flatMap(identity).filter(_.age > "23")collect()


// Work with primitive type
val ds = df.as[(Seq[(String, String, String)], String)]
ds.map(_._1).flatMap(identity).collect()

val students = df.select("students").as[Seq[(String, String, String)]]
students.flatMap(identity).collect()


// work with Row

val students = filtered.select("students")
students.show()
students
  .map(row =>
    row match {
      case Row(s: Seq[Row]) =>
        s.map { case Row(age: String, id: String, name: String) => Student(age, id, name) }
    })
  .flatMap(identity)
  .collect()

import org.apache.spark.sql.types.DataTypes._

val studentFields = createStructField("age", StringType, true) :: createStructField("id", StringType, true) :: createStructField(
  "name",
  StringType,
  true) :: Nil
val studentSchema = createStructType(studentFields.toArray)
val studentsFields = createStructField("students", createArrayType(studentSchema), true) :: Nil
val studentsSchema = createStructType(studentsFields.toArray)
val rowEncoder = RowEncoder(studentsSchema)

val students = filtered.select("students").as[Row](rowEncoder)
val encoder = Encoders.tuple(Encoders.STRING, Encoders.STRING)
val df = students
  .map(row =>
    row match {
      case Row(s: Seq[Row]) =>
        s.map { case Row(age: String, id: String, name: String) => (age, name) }
    })
  .flatMap(identity)(encoder)
// the point is with encoder, we can still use the result as Dataset.
df.select("_1").show()

// and join
filtered.join(df.withColumn("age", col("_1")), "age").show()

}

{
import $ivy.`com.github.benfradet::struct-type-encoder:0.5.0`

import ste.StructTypeEncoder
import ste.StructTypeEncoder._
case class Student(age:String, id:String, name:String)

// force schema
val df = spark.read.schema(StructTypeEncoder[Student].encode).json(jsonData.toDS)
val ds = df.as[Student]

// don't know why do we need StructTypeEncoder
val ds = spark.read.schema(Encoders.product[Student].schema).json(jsonData.toDS).as[Student]
ds.show()
}

{
// https://intellipaat.com/community/9479/encoder-error-while-trying-to-map-dataframe-row-to-updated-row
// https://stackoverflow.com/questions/39433419/encoder-error-while-trying-to-map-dataframe-row-to-updated-row
import org.apache.spark.sql.functions.{when, lower}

val df = Seq((2012, "Tesla", "S"), (1997, "Ford", "E350"), (2015, "Chevy", "Volt")).toDF("year", "make", "model")
df.withColumn("make", when(lower($"make") === "tesla", "S").otherwise($"make"))
df.show()

import spark.implicits._

case class Record(year: Int, make: String, model: String)

val ds = df.as[Record].map {
  case tesla if tesla.make.toLowerCase == "tesla" => tesla.copy(make = "S")
  case rec => rec
}
ds.show()


import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row

// Yup, it would be possible to reuse df.schema here
val schema = StructType(Seq(StructField("year", IntegerType), StructField("make", StringType), StructField("model", StringType)))
val encoder = RowEncoder(schema)

val ds = df.map {
  case Row(year, make: String, model) if make.toLowerCase == "tesla" => Row(year, "S", model)
  case row => row
}(encoder)
ds.show()
}
