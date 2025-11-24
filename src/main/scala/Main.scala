import parser.parseUtils
import parser.yamlParser
import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val fileContents = Source.fromFile("/home/saku/Code/scala/asteroid/src/main/resources/examples/asteroid.belt").mkString
    val config: Option[Map[String, Any]] = yamlParser.parseMapping(fileContents) 
    config match {
      case Some(result) => println(result)
      case None => println("Error occured")
    }
  }
}
