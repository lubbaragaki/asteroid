import org.virtuslab.yaml.*
import scala.io.Source
import core.Prepare

case class Output(path: String, format: String) derives YamlCodec
case class Comments(single: Seq[String], multiline: Seq[String]) derives YamlCodec
case class Config(build: Seq[String], run: Seq[String], comments: Comments, wordlists: Seq[String], outputs: Map[String, Output]) derives YamlCodec


object ConfigLoader {
  def findFile() = {
    val fileName = "asteroid.belt"
    // Search upwards only, from the current directory until asteroid.belt
    // is found or the user's homedir is encoutered, if homedir was encountered 
    // then exit with an error message 'Configuration file not found'. 
    val homedir = os.home
    val workdir = os.pwd
    Source.fromFile(s"$fileName").mkString
  }
}


object Main {
  def main(args: Array[String]): Unit = {
    Prepare.replicateDirectory(os.pwd / "src/main/resources/examples")
//    var err = false
 //   val fileContents = findFile()
//    val config = fileContents.as[Config] match {
//      case Right(validConfig) => validConfig
//      case Left(error) => err = true; error
//    }
//    if(err == true) println(s"Error while parsing your configuration file:\n$config\n----------------\nMake sure it is a valid yaml file and similar to the example config"); sys.exit(1)
//    println(config)
  }
}
