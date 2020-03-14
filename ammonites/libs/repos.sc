import $exec.`compatPost`

/**
 * ## Nexus Repositories ##
 */
import java.util.Base64
import ammonite.ops.{home, Path}
import $ivy.`com.typesafe:config:1.4.0`
import com.typesafe.config.ConfigFactory
import scala.util.{Failure, Success, Try}

def resultOf[T](action: => T): Option[T] =
  Try(action) match {
    case Success(v) => Some(v)
    case Failure(_) => None
  }

val envNexusCredential = resultOf((sys.env("NEXUS_USERNAME"), sys.env("NEXUS_PASSWORD")))

def gradleNexusCredential(gradleProperties: Path): Option[(String, String)] = {
  val gradleConfig = ConfigFactory.parseFile(gradleProperties.toIO)

  val nexusUsername = resultOf(gradleConfig.getString("nexusUsername"))
  val nexusEncPassword = resultOf {
    val encoded = gradleConfig.getString("nexusEncPassword")
    Base64.getDecoder.decode(encoded).map(_.toChar).mkString.trim
  }
  val nexusPassword = resultOf(gradleConfig.getString("nexusPassword"))
  val pickedPassword = Seq(nexusEncPassword, nexusPassword).find(_.isDefined).flatten

  (nexusUsername, pickedPassword) match {
    case (Some(username), Some(password)) => Some((username, password))
    case _ => None
  }
}

val gradleProperties = home / ".gradle" / "gradle.properties"
val nexusCredential = Seq(envNexusCredential, gradleNexusCredential(gradleProperties)).find(_.isDefined).flatten

// internal / private repositories
val nexusRepoUrlList = Seq[String]()
val nexusRepos = nexusRepoUrlList.map(url =>
  nexusCredential match {
    case Some((username, password)) => AmmMavenRepository.of(url, username, password)
    case _ => AmmMavenRepository.of(url)
  })

// external / 3rd party repos
case class RepoMeta(url: String, credential: Option[(String, String)] = None)
val moreRepoList = Seq(RepoMeta("https://jitpack.io"))
val moreRepos = moreRepoList.map(_ match {
  case RepoMeta(url, Some((username, password))) => AmmMavenRepository.of(url, username, password)
  case RepoMeta(url, _) => AmmMavenRepository.of(url)
})

val allRepos = nexusRepos ++ moreRepos
if (ammonite.Constants.version <= "1.6.7") {
  interp.repositories() ++= allRepos
} else {
  interp.repositories.update(interp.repositories() ++ allRepos)
}


/**
 * ## Local Repositories ##
 *
 * https://github.com/lihaoyi/Ammonite/pull/612, Resolution of local Maven artifacts does not work
 */
val mavenRepoLocal = AmmMavenRepository.of("file://" + java.lang.System.getProperties.get("user.home") + "/.m2/repository/")
interp.repositories() ++= Seq(mavenRepoLocal)
