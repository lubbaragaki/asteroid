import org.virtuslab.yaml.*
import scala.io.Source

case class Output(path: String, format: String) derives YamlCodec
case class Comments(single: Seq[String], multiline: Seq[String]) derives YamlCodec
case class Config(build: Seq[String], run: Seq[String], comments: Comments, wordlists: Seq[String], outputs: Map[String, Output]) derives YamlCodec

object Main {
  def main(args: Array[String]): Unit = {
    var err = false
    val fileContents = Source.fromFile("/home/saku/Code/scala/asteroid/src/main/resources/examples/asteroid.belt").mkString
    val config = fileContents.as[Config] match {
      case Right(validConfig) => validConfig
      case Left(error) => err = true; error
    }
    if(err == true) println(s"Error while parsing your configuration file:\n$config") else println(config)
  }
}
