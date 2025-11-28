package loader
import org.virtuslab.yaml.*
import core.Prepare
import os.Path

object Loader {

  // Receives the path to the root of the project
  // which is the directory of asteroid.belt, computed
  // by findRoot() in the main procedure
  def replicateDirectory(projectRoot: Path) = {
    def copyAny(path: Path) = {
      val name = path.getSegment(path.segments.length-1)
      os.copy.over(path, projectRoot / ".asteroid" / name)
      }
    val homedir = os.home
    os.makeDir.all(projectRoot / ".asteroid")
    val paths = os.list(projectRoot).filter(p => p != (projectRoot / ".asteroid") && p != (projectRoot / "asteroid.belt"))
    paths.map(p => {copyAny(p)})
  }

  // Recursively delete the .asteroid directory
  // situated at the project root
  def clean(projectRoot: String) = {
    
  }

  // Find the root directory of the project
  // based on the presence of an asteroid.belt
  // file in there
  def findRoot() = {
    val fileName = "asteroid.belt"
    val homedir = os.home
    var workdir = os.pwd
    while(workdir != homedir && !os.exists(workdir / fileName)) {
      workdir = workdir / os.up
    }
    workdir match {
      case homedir => Left("Error: Configuration file 'asteroid.belt' not found\nNote: asteroid looks for the file upwards from the directory it was called from")
      case _ => Right(workdir)
    }
  }

  // Receives a mapping from variable names to wordlist names
  // and returns a mapping from variables to wordlist contents
  // in the form of an array of string
  def loadWordlists(wordlists: Map[String, String]) = {
    val wordlistsDir = os.home / ".local/share/asteroids/wordlists"
    var wordlistsContents: Map[String, Array[String]] = Map()
    for(variable <- wordlists.keys.toArray) {
      wordlistsContents += (variable -> os.read(wordlistsDir / wordlists(variable)).split('\n'))
    }
    wordlistsContents
  }

}

