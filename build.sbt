
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
  "com.typesafe.akka" %% "akka-http"      % "10.2.6",
  "com.typesafe.akka" %% "akka-stream"    % "2.6.15",
  "javax.xml.bind"     % "jaxb-api"       % "2.3.1",
  "org.testcontainers" % "testcontainers" % "1.15.3"    % Test,
  "com.google.guava"   % "guava"          % "30.1.1-jre"  % Test,
  "org.scalatest"     %% "scalatest"      % "3.2.9"     % Test,
  "io.rest-assured"    % "rest-assured"   % "4.4.0"     % Test,
  "org.slf4j"          % "slf4j-api"      % "1.7.31"    % Test,
  "org.slf4j"          % "slf4j-simple"   % "1.7.31"    % Test
)

Compile / guardrailTasks := List(
  ScalaClient(
    file("src/main/resources/swagger/kayenta-api.json"),
    pkg="demo.clients",
    imports=List("_root_.support.PositiveLong")),
)
