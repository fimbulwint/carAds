name := "Car-Service"
version := "1.0"
scalaVersion := "2.11.8"

libraryDependencies += "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.7"
libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test

lazy val root = (project in file(".")).enablePlugins(PlayScala)
