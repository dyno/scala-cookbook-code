
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
ammonite.shell.Configure(interp, repl, wd)

interp.configureCompiler(_.settings.nowarn.value = false)

// -----------------------------------------------------------------------------
// repos.sc - to access repos
// sparkjars.sc - for AmmoniteSparkSession
val modules = List("repos.sc", "sparkjars.sc")
val modulePaths: List[Path] = modules map { pwd / _ } filter { os.exists }
if (modulePaths.nonEmpty) {
  modulePaths foreach { interp.load.module }
} else { // fallback to home/.ammonite, e.g. inside docker.
  modules map { home / ".ammonite" / _ } filter { os.exists } foreach { interp.load.module }
}
