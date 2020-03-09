/**
 * ## Load jars ##
 */
import ammonite.ops._

// e.g. /opt/.ivy2/local/sh.almond/ammonite-spark_2.12/0.8.0+16-fc59944f-SNAPSHOT/jars/ammonite-spark_2.12.jar => ammonite-spark
val toPackageName = (jarPath: Path) => jarPath.baseName.split("-").dropRight(1).mkString("-")
val jarFilter = (jarPath: Path) =>
  jarPath.ext == "jar" && !List("-tests", "-javadoc", "-sources", "jre7", "empty-to-avoid-conflict-with-guava").exists(
    jarPath.baseName.endsWith)

if (sys.env.get("SPARK_HOME").nonEmpty) {
  // ## ${SPARK_HOME}/jars ##
  val sparkHomeJars = ls ! Path(sys.env("SPARK_HOME")) / 'jars |? { _.ext == "jar" }
  val sparkJarToPath = sparkHomeJars.filter(jarFilter).map(p => (toPackageName(p), p)).toMap

  // ## ${SPARK_DIST_CLASSPATH} ##
  // inside docker, SPARK_DIST_CLASSPATH is not set.
  val distClasspath = sys.env.get("SPARK_DIST_CLASSPATH").getOrElse(os.proc("hadoop", "classpath").call().out.trim)
  val libs = distClasspath.split(":").filter(_ != "")
  val jars = libs.filter(_.endsWith("jar")).map(Path(_))
  val paths = libs.filter(!_.endsWith("jar")).map(_.replace("*", "")).map(Path(_)).toSet
  val jarsInPaths = paths.map(p => ls ! p |? { _.ext == "jar" }).flatMap(identity)
  val hadoopJarToPath = (jars ++ jarsInPaths).filter(jarFilter).map(p => (toPackageName(p), p)).toMap

  // XXX: if jar exists in both, use spark one.
  val allJars = hadoopJarToPath ++ sparkJarToPath
  val conflictExcludes = Seq("jetty-")
  val filteredAllJars = allJars.filterKeys(key => !conflictExcludes.exists(key.startsWith))
  // val filteredAllJars = allJars
  (paths ++ filteredAllJars.values).foreach { interp.load.cp }
}
