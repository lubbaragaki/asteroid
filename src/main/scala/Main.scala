import org.virtuslab.yaml.*
import core.Prepare
import os.Path

case class Output(path: String, format: String) derives YamlCodec
case class Config(build: Seq[String], run: Seq[String], wordlists: Map[String, String], outputs: Map[String, Output]) derives YamlCodec


object Loader {
  def findRoot() = {
    val fileName = "asteroid.belt"
    val homedir = os.home
    var workdir = os.pwd
    while(workdir != homedir && !os.exists(workdir / fileName)) {
      workdir = workdir / os.up
    }
    workdir match {
      case homedir => Right(os.read(workdir / "asteroid.belt").mkString, workdir)
      case _ => Left("Error: Configuration file 'asteroid.belt' not found\nNote: asteroid looks for the file upwards from the directory it was called from")
    }
  }
  def loadWordlists(wordlists: Map[String, Array[String]]) = {
    val wordlistsDir = os.home / ".local/share/asteroids/wordlists"
    var wordlistsContents: Map[String, String] = Map()
    for(variable <- wordlists.keys.toArray) {
      wordlistsContents += (variable -> os.read(wordlistsDir / wordlists(variable)).split('\n'))
    }
    wordlistsContents
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    var dir: Path = os.pwd
    var file: String = ""
    val wordlists = Loader.loadWordlists
    Loader.findRoot() match {
      case Right(fileContents, dirname) => dir = dirname; file = fileContents
      case Left(errorString) => {
        println(errorString)
        sys.exit(1)
      }
    }
    Prepare.replicateDirectory(dir)
    val config = file.as[Config] match {
      case Right(validConfig) => validConfig
      case Left(error) => {
        println(s"Error while parsing your configuration file:\n$error\n----------------\nMake sure it is a valid yaml file and similar to the example config")
        sys.exit(1)
      }
    }
  }
}
