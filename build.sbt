ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.danielroy"
ThisBuild / name := "SocialNetworkPresence"

val ZIOVersion = "2.0.0-RC6"
val ZIOJSONVersion = "0.3.0-RC8"

lazy val root = (project in file("."))
  .settings(
    name := "SocialNetworkPresence",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % ZIOVersion,
      "dev.zio" %% "zio-json" % ZIOJSONVersion
    )
  )
