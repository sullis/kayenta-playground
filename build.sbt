
enablePlugins(GuardrailPlugin)

name := "kayenta-playground"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.13.6"

scalacOptions += "-Xexperimental"

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "javax.xml.bind"     % "jaxb-api"       % "2.3.1",
  "org.testcontainers" % "testcontainers" % "1.19.8"    % Test,
  "com.google.guava"   % "guava"          % "31.0.1-jre"  % Test,
  "org.scalatest"     %% "scalatest"      % "3.2.10"     % Test,
  "io.rest-assured"    % "rest-assured"   % "4.4.0"     % Test,
  "org.slf4j"          % "slf4j-api"      % "1.7.32"    % Test,
  "org.slf4j"          % "slf4j-simple"   % "1.7.32"    % Test
)

Compile / guardrailTasks := List(
  ScalaClient(
    file("src/main/resources/swagger/kayenta-api.json"),
    pkg="demo.clients",
    imports=List("_root_.support.PositiveLong")),
)
