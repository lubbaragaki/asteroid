import org.virtuslab.yaml.*
import scala.io.Source

case class Output(path: String, format: String) derives YamlCodec
case class Comments(single: Seq[String], multiline: Seq[String]) derives YamlCodec
case class Config(build: Seq[String], run: Seq[String], comments: Comments, wordlists: Seq[String], outputs: Map[String, Output]) derives YamlCodec

object Main {
  def main(args: Array[String]): Unit = {
    val fileContents = Source.fromFile("/home/saku/Code/scala/asteroid/src/main/resources/examples/asteroid.belt").mkString
    val config = fileContents.as[Config] 
    println(config)
  }
}
