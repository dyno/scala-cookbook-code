// https://github.com/lihaoyi/Ammonite/issues/1003, Cousier update changed API
import ammonite.ops.pwd

if (ammonite.Constants.version < "1.7.1")
  interp.load.module(pwd / "compat_coursier.sc")
else
  interp.load.module(pwd / "compat_coursierapi.sc")


@


/**
 * ## Signifyd Repositories ##
 */
import java.util.Base64
import ammonite.ops.{Path, home}
import $ivy.`com.typesafe:config:1.3.3`
import com.typesafe.config.ConfigFactory

def gradleNexusCredential(gradleProperties: Path): (String, String) = {

  val gradleConfig = ConfigFactory.parseFile(gradleProperties.toIO)
  val nexusUsername = gradleConfig.getString("nexusUsername")
  val nexusPassword = if (gradleConfig.hasPath("nexusEncPassword")) {
    val nexusEncPassword = gradleConfig.getString("nexusEncPassword")
    Base64.getDecoder.decode(nexusEncPassword).map(_.toChar).mkString.trim
  } else {
    gradleConfig.getString("nexusPassword")
  }

  (nexusUsername, nexusPassword)
}

val nexusRepoUrlList = List[String]()

val (optNexusUsername, optNexusPassword) =
  (sys.env.get("NEXUS_USERNAME"), sys.env.get("NEXUS_PASSWORD"))
val gradleProperties = home / ".gradle" / "gradle.properties"

val (nexusUsername, nexusPassword): (String, String) =
  if (optNexusUsername.nonEmpty && optNexusPassword.nonEmpty) {
    (optNexusUsername.get, optNexusPassword.get)
  } else if (os.exists(gradleProperties)) {
    gradleNexusCredential(gradleProperties)
  } else {
    ("", "")
  }

if (nexusUsername != "" && nexusPassword != "") {
  interp.repositories() ++= nexusRepoUrlList.map(url => MavenRepo.of(url, nexusUsername, nexusPassword))
}

/**
 * ## Local Repositories ##
 *
 * https://github.com/lihaoyi/Ammonite/pull/612, Resolution of local Maven artifacts does not work
 */
val mavenRepoLocal = MavenRepo.of("file://" + java.lang.System.getProperties.get("user.home") + "/.m2/repository/")
interp.repositories() ++= Seq(mavenRepoLocal)
