import ammonite.ops.{pwd, write, RelPath}
import pprint.log

// https://github.com/lihaoyi/Ammonite/issues/1003, Cousier update changed API
// https://github.com/lihaoyi/Ammonite/issues/149, Can't load modules from modules

val (compatLib, compatLibPost) = ammonite.Constants.version.split('.').take(2).map(_.toInt) match {
  // i.e. "1.6.7" => 16
  case Array(major, minor) if major * 10 + minor <= 16 =>
    (
      "libs/_compatCoursier.sc",
      """
     |import ammonite.$file.dummy.libs._compatCoursier._
     """.stripMargin)
  case _ =>
    (
      "libs/_compatCoursierApi.sc",
      """
      |import ammonite.$file.dummy.libs._compatCoursierApi._
      """.stripMargin)
}

val compatLibPath = pwd / RelPath(compatLib)
log(s"loading $compatLibPath")
interp.load.module(compatLibPath)
log(s"loaded  $compatLibPath")

val compatLibPostFiles = Seq("compatPost.sc", "libs/compatPost.sc")
log(s"generate $compatLibPostFiles")
compatLibPostFiles.foreach(compatLibPostFile => write.over(pwd / RelPath(compatLibPostFile), compatLibPost))
