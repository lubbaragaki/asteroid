//import org.virtuslab.yaml.*
//import core.Template
//import os.Path
//
//> using scala 3.3.6
//> using dep com.lihaoyi::os-lib:0.11.6
//> using dep org.virtuslab::scala-yaml:0.3.1
//> using dep org.luaj:luaj-jse:3.0.1
//> using dep com.lihaoyi::fansi:0.5.1
//> using file "./Loader.scala"
//> using file "./Core.scala"

import os.*
import org.virtuslab.yaml.*
import fansi.Color
import loader.Loader
import core.CoreRun

case class Config(build: Seq[String], run: Seq[String], wordlists: Map[String, String], script: Option[String], files: Seq[String]) derives YamlCodec

object Main {

  def getRoot(): Path = {
    val rootDir = Loader.findRoot()
    if(!os.exists(rootDir / "asteroid.belt")) {
        println("'asteroid.belt' configuration file not found:\nAdd it to the root of your project and run asteroid from there or from that directory or one of its subdirectories")
        sys.exit(1)
    } else {
        rootDir
    }
  }

  def getYamlConfig(file: Path) = {
    os.read(file).as[Config] match {
      case Right(validConfig) => validConfig
      case Left(error) => {
        println(s"Error while parsing your configuration file:\n$error\n----------------\nMake sure it is a valid yaml file and similar to the example config")
        sys.exit(1)
      }
    }
  }

  def main(args: Array[String]): Unit = {
    
    var noOutput = false
    if(args.length > 0 && args(0) == "--no-output") {
      noOutput = true
    }
    val rootDir = getRoot()
    val config = getYamlConfig(rootDir / "asteroid.belt")
    Loader.replicateDirectory(rootDir)
    val wordlists = Loader.loadWordlists(config.wordlists)
    val combinations = CoreRun.getCombinations(wordlists)
    CoreRun.mainProc(config.files, rootDir, combinations, config.build, config.run, config.script, noOutput)
    Loader.clean(rootDir)

  }
}
