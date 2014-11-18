package demo
package app

import java.io._
import java.net._
import scala.reflect.{classTag, ClassTag}

object Test extends App {
  // TODO: would be nice to see how SBT's run can also do custom stuff
  // like, for instance, passing the output directory of the implementation project here as system var
  val root = new File("./implementation/target/scala-2.11/classes")
  val cl = new URLClassLoader(Array(root.toURI.toURL), getClass.getClassLoader)
  def allClassesInDir(dir: File): List[Class[_]] = {
    val dirs = dir.listFiles.filter(_.isDirectory).toList
    val classfiles = dir.listFiles.filter(f => f.isFile && f.getName.endsWith(".class")).toList
    dirs.flatMap(allClassesInDir) ++ classfiles.map(classfile => {
      val className = {
        if (root.getCanonicalPath == dir.getCanonicalPath) {
          classfile.getName.stripSuffix(".class")
        } else {
          val packagePath = dir.getCanonicalPath.substring(root.getCanonicalPath.length + 1)
          val packageName = packagePath.replace(File.separator, ".")
          packageName + "." + classfile.getName.stripSuffix(".class")
        }
      }
      cl.loadClass(className)
    })
  }
  val allClasses = allClassesInDir(root)
  val subclasses = allClasses.filter(clazz => classOf[api.Base].isAssignableFrom(clazz))
  val instances = subclasses.map(clazz => {
    val instance = {
      try clazz.getDeclaredField("MODULE$").get(null)
      catch { case ex: NoSuchFieldException => clazz.newInstance }
    }
    instance.asInstanceOf[api.Base]
  })
  instances.foreach(_.apply())
}