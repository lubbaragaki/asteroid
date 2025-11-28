import org.virtuslab.yaml.*
import core.CoreRun
import core.Template
import os.Path
import loader.Loader

case class Output(path: String, format: String) derives YamlCodec
case class Config(build: Seq[String], run: Seq[String], wordlists: Map[String, String], outputs: Map[String, Output]) derives YamlCodec

object Main {

  def getRoot() = {
    Loader.findRoot() match {
      case Right(dirname) => dirname
      case Left(errorString) => {
        println(errorString)
        sys.exit(1)
      }
    }
  }

  def getYamlConfig(file: Path) = {
    file.as[Config] match {
      case Right(validConfig) => validConfig
      case Left(error) => {
        println(s"Error while parsing your configuration file:\n$error\n----------------\nMake sure it is a valid yaml file and similar to the example config")
        sys.exit(1)
      }
    }
  }

  def main(args: Array[String]): Unit = {
    
    val rootDir = getRoot()
    val wordlists = Loader.loadWordlists
    Loader.replicateDirectory(dir)
    val config = getYamlConfig(rootDir)
  
  }
}
