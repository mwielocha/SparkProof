import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

object SparkProofBuild extends Build {

  val appName         = "SparkProof"
  val appVersion      = "1.0.0-SNAPSHOT"

  object Version {
    val scala     = "2.10.3"
    val scalaTest = "2.0.RC1-SNAP6"
  }

  val appDependencies =  Seq(
    ("org.apache.spark" %% "spark-core" % "0.9.0-incubating" % "provided")
      .exclude("org.mortbay.jetty", "servlet-api")
      .exclude("commons-beanutils", "commons-beanutils-core")
      .exclude("commons-collections", "commons-collections")
      .exclude("commons-collections", "commons-collections")
      .exclude("com.esotericsoftware.minlog", "minlog")
      .exclude("org.eclipse.jetty.orbit", "javax.servlet"),
    ("com.tuplejump" %% "calliope" % "0.9.0-U1-EA")
      .exclude("org.mortbay.jetty", "servlet-api")
      .exclude("commons-beanutils", "commons-beanutils-core")
      .exclude("commons-collections", "commons-collections")
      .exclude("commons-collections", "commons-collections")
      .exclude("com.esotericsoftware.minlog", "minlog")
      .exclude("org.eclipse.jetty.orbit", "javax.servlet")
      .exclude("org.apache.avro", "avro")
      .exclude("org.apache.avro", "avro-ipc")
      .exclude("org.slf4j", "slf4j-log4j12")
      .exclude("org.jboss.netty", "netty")
  )


  val buildSettings = Defaults.defaultSettings ++ Seq(
    version := appVersion,
    organization := "anonymous",
    exportJars := true,
    scalaVersion        := Version.scala,
    javacOptions ++= Seq("-source", "1.6") ++ Seq("-target", "1.6"),
    unmanagedClasspath in Runtime += file("conf/")
  )

  val main = Project(id = appName, base = file("."),
    settings = buildSettings ++ Seq(libraryDependencies ++= (appDependencies)) ++ assemblySettings
  )
}
