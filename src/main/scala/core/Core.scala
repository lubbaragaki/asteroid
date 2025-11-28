package core
import os.Path
import scala.sys.process._


object CoreRun {
  def buildAndRun(build: Seq[String], run: Seq[String]) = {
    for(cmd <- build) {
      Process(cmd)
    }
    for(cmd <- run) {
      Process(cmd)
    }
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
        replaceVariable(line, variables(key))
      } else {
        line
      }
    })
    lines.map(x => x ++ "\n").mkString
  }
}
