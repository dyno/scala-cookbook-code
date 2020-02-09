name := "Hello Test #1"
version := "1.0"
scalaVersion := "2.12.10"
resolvers += "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"
// https://doc.akka.io/docs/akka/current/actors.html
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.3"

lazy val hello = (project in file("hello"))

