package demo
package app

import java.io._
import java.net._

object Test extends App {
  // TODO: would be nice to see how SBT's run can also do custom stuff
  // like, for instance, passing the output directory of the implementation project here as system var
  val dir = new File("./implementation/target/scala-2.11/classes")
  val cl = new URLClassLoader(Array(dir.toURI.toURL), getClass.getClassLoader)
  println(cl.loadClass("Dummy"))
}