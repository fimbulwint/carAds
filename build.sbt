name := "Car-Service"
version := "1.0"
scalaVersion := "2.11.8"

libraryDependencies += "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
