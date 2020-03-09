import coursier.core.Authentication
import coursier.MavenRepository
import coursier._

object AmmMavenRepository {
  def of(url: String, username: String, password: String): MavenRepository =
    MavenRepository(url, authentication = Some(Authentication(username, password)))

  def of(url: String): MavenRepository = MavenRepository(url)
}

object AmmSpark {
  val depOfAmmoniteSpark = dep"sh.almond::ammonite-spark:0.4.2"
}
