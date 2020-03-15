// https://jaceklaskowski.gitbooks.io/mastering-spark-sql/spark-sql-schema.html

{
import org.apache.spark.sql.types.StructType
val schemaUntyped = new StructType()
  .add("a", "int")
  .add("b", "string")

// alternatively using Schema DSL
val schemaUntyped_2 = new StructType()
  .add($"a".int)
  .add($"b".string)

// it is equivalent to the above expressions
import org.apache.spark.sql.types.{IntegerType, StringType, LongType}
val schemaTyped = new StructType()
  .add("a", IntegerType)
  .add("b", StringType)
schemaTyped.printTreeString

// the singleton DataTypes class with static methods to create schema types.
import org.apache.spark.sql.types.DataTypes._
val schemaWithMap = StructType(StructField("map", createMapType(LongType, StringType), false) :: Nil)

schemaWithMap.printTreeString
}
