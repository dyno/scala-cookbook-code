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

/**
 * ## main ##
 */
case class RepoMeta(url: String, credential: Option[(String, String)] = None)

// https://github.com/lihaoyi/Ammonite/pull/612, Resolution of local Maven artifacts does not work
val localRepoUrls = Seq(s"""file://${sys.env("HOME")}/.m2/repository/""")
val localRepos = localRepoUrls.map(url => RepoMeta(url, None))

// internal / private repositories
val nexusRepoUrls = Seq[String]()
val nexusRepos = nexusRepoUrls.map(url => RepoMeta(url, envNexusCredential))

// external / 3rd party repos
val thirdPartyRepos = Seq(RepoMeta("https://jitpack.io"))

val allRepos = (localRepos ++ nexusRepos ++ thirdPartyRepos).map(_ match {
  case RepoMeta(url, Some((username, password))) => AmmMavenRepository.of(url, username, password)
  case RepoMeta(url, _) => AmmMavenRepository.of(url)
})

if (ammonite.Constants.version <= "1.6.7") {
  interp.repositories() ++= allRepos
} else {
  interp.repositories.update(interp.repositories() ++ allRepos)
}
