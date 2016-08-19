name := """play-guice-mirage-java-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  //javaEbean,
  cache,
  javaWs,
  Common.libGuice,
  "org.springframework" % "spring-jdbc" % "3.2.4.RELEASE",
  "org.springframework" % "spring-aop" % "3.2.4.RELEASE",
  //"com.h2database" % "h2" % "1.4.178",
  "mysql" % "mysql-connector-java" % "5.1.16",
  "jp.sf.amateras" % "mirage" % "1.2.3"
)

resolvers += Common.additionalResolvers

PlayKeys.ebeanEnabled := false

// eclipse settings

EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE17)

EclipseKeys.withSource := true

EclipseKeys.eclipseOutput := Some(".target")

EclipseKeys.skipParents in ThisBuild := false
