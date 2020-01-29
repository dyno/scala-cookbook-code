import coursier.core.Authentication
import coursier.MavenRepository

object MavenRepo {
  def of(url: String, username: String, password: String): MavenRepository = {
    MavenRepository(url, authentication = Some(Authentication(username, password)))
  }

  def of(url: String): MavenRepository = {
    MavenRepository(url)
  }
}
