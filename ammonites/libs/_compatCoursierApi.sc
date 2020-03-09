import coursierapi.{Credentials, Dependency, MavenRepository, ScalaVersion}

object AmmMavenRepository {
  def of(url: String, username: String, password: String): MavenRepository =
    MavenRepository.of(url).withCredentials(Credentials.of(username, password))

  def of(url: String): MavenRepository = MavenRepository.of(url)
}

object AmmSpark {
  val depOfAmmoniteSpark = Dependency.parse("sh.almond::ammonite-spark:0.9.0", ScalaVersion.of(scala.util.Properties.versionNumberString))
}
