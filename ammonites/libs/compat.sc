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

log(s"loading $compatLib")
interp.load.module(pwd / RelPath(compatLib))
log(s"loaded  $compatLib")

// XXX: 1.6.7 throws exception
// write.over(pwd / RelPath("libs/compatPost.sc"), compatLibPost)
// https://stackoverflow.com/questions/6879427/scala-write-string-to-file-in-one-statement
import java.nio.file.{Paths, Files}
import java.nio.charset.StandardCharsets

val compatLibPostFiles = Seq("compatPost.sc", "libs/compatPost.sc")
log(s"write $compatLibPostFiles")
compatLibPostFiles.foreach(compatLibPostFile => Files.write(Paths.get(compatLibPostFile), compatLibPost.getBytes(StandardCharsets.UTF_8)))
