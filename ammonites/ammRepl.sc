// repl internal state
pprint.pprintln(interp.repositories())
pprint.pprintln(repl.sess.frames(0).classpath)
pprint.pprintln(repl.fullImports)

// function signature
val numbers = List(1, 2, 3)
numbers.foldLeft // <TAB>
// override def foldLeft[B](z: B)(op: (B, A) => B): B

// import file
import $file.libs.sparkSession
val spark = ammonite.$sess.libs.sparkSession


// versions
ammonite.Constants.version
scala.util.Properties.versionNumberString
org.apache.spark.SPARK_VERSION

repl.sess.frames(0).classpath.map(_.toString).map(_.drop(5)).map(Path(_).last).filter(_.contains("scala-reflect"))

// log
pprint.log(scala.util.Properties.versionNumberString)
pprint.log(s"${1 + 1}")

// desugar
import $ivy.`io.get-coursier::coursier:2.0.16`
import coursier._
desugar(dep"sh.almond::ammonite-spark:0.10.1")
