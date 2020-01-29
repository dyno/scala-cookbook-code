import coursierapi.{MavenRepository, Credentials}

object MavenRepo {
  def of(url: String, username: String, password: String): MavenRepository = {
    MavenRepository.of(url).withCredentials(Credentials.of(username, password))
  }

  def of(url: String): MavenRepository = {
    MavenRepository.of(url)
  }
}
