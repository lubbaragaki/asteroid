package core
import os.Path

object Prepare {
  // Receives the path to the root of the project
  // which is the directory of asteroid.belt
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

  def clean(projectRoot: String) = {
    
  }
}



object Template {
  def lineIsVariable(line: String) = {
    line.contains("((") && line.contains("))") && line.contains("{") && line.contains("}") && line.contains("%%")
  }
  def replaceVariable(line: String, value: String) = {
    val newline = line.slice(line.lastIndexOf("((")+2,line.lastIndexOf("))"))
    newline.replace("%%", value)
  }
  def parseMapKey(line: String) = {
    line.slice(line.lastIndexOf("{")+1,line.lastIndexOf("}"))
  }
  def templater(text: String, variables: Map[String, String]) = {
    var lines = text.split('\n')
    lines = lines.map(line => {
      if(lineIsVariable(line)){
        val key = parseMapKey(line)
        replaceVariable(line, key)
      } else {
        line
      }
    })
    lines.map(x => x ++ "\n").mkString
  }
}
