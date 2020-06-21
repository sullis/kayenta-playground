
enablePlugins(GuardrailPlugin)

name := "kayenta-demo"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.13.2"

scalacOptions += "-Xexperimental"

val circeVersion = "0.13.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"      % "10.1.12",
  "com.typesafe.akka" %% "akka-stream"    % "2.6.6",
  "javax.xml.bind"     % "jaxb-api"       % "2.3.1",
  "org.testcontainers" % "testcontainers" % "1.14.3"    % Test,
  "com.google.guava"   % "guava"          % "29.0-jre"  % Test,
  "org.scalatest"     %% "scalatest"      % "3.2.0"     % Test,
  "io.rest-assured"    % "rest-assured"   % "4.3.0"     % Test,
  "org.slf4j"          % "slf4j-api"      % "1.7.30"    % Test,
  "org.slf4j"          % "slf4j-simple"   % "1.7.30"    % Test
)

guardrailTasks in Compile := List(
  ScalaClient(
    file("src/main/resources/swagger/kayenta-api.json"),
    pkg="com.example.clients.foobar",
    imports=List("_root_.support.PositiveLong")),
)
