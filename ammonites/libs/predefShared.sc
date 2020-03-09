/**
 * ## Ammonite Shell ##
 *
 * http://ammonite.io/#Ammonite-Shell
 * https://git.io/vHaKQ
 */

interp.load.ivy(
  "com.lihaoyi" %
    s"ammonite-shell_${scala.util.Properties.versionNumberString}" %
    ammonite.Constants.version)

@  // multistage scripts. http://ammonite.io/#Multi-stageScripts

// -----------------------------------------------------------------------------
val shellSession = ammonite.shell.ShellSession()
import shellSession._
import ammonite.ops._
import ammonite.shell._

// -----------------------------------------------------------------------------
import pprint.log

// repos.sc - to access repos
// sparkjars.sc - for AmmoniteSparkSession
val modules = List("repos.sc", "sparkjars.sc")
val libModulePaths: List[Path] = modules.map(m => pwd / "libs" / m).filter(os.exists)
val homeModulePaths: List[Path] = modules.map(home / ".ammonite" / _).filter(os.exists)
Seq(libModulePaths, homeModulePaths).find(_.nonEmpty) match {
  case Some(modulePaths) =>
    modulePaths.foreach { module =>
      log(s"loading $module")
      interp.load.module(module)
      log(s"loaded $module")
    }
  case _ =>
}
