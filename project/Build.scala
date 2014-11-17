import sbt._
import Keys._

object build extends Build {
  lazy val sharedSettings = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.11.4",
    resolvers += Resolver.sonatypeRepo("snapshots"),
    resolvers += Resolver.sonatypeRepo("releases"),
    scalacOptions ++= Seq("-deprecation", "-feature"),
    parallelExecution in Test := false, // hello, reflection sync!!
    logBuffered := false
  )

  lazy val demo = Project(
    id   = "demo",
    base = file("demo")
  ) settings (
    sharedSettings : _*
  ) settings (
  ) dependsOn (interface)

  lazy val interface = Project(
    id   = "interface",
    base = file("interface")
  ) settings (
    sharedSettings: _*
  ) settings (
  )

  lazy val implementation = Project(
    id   = "implementation",
    base = file("implementation")
  ) settings (
    sharedSettings: _*
  ) settings (
  ) dependsOn (interface)

  lazy val root = Project(
    id   = "root",
    base = file("root")
  ) settings (
    sharedSettings: _*
  ) settings (
    run in Compile := {
      val _ = (compile in Compile).value
      (run in Compile in demo).evaluated
    }
  ) dependsOn (demo, interface, implementation)
}
