package core
import os.Path
import os._
import scala.sys.process._
import fansi._
import scala.sys.process._
import collection.mutable.StringBuilder

object CoreRun {

  def buildAndRun(dir: Path, build: Seq[String], run: Seq[String]) = {

    var result = os.proc(Seq("ls")).call(check = false)

    def executeCommand(cmd: String) = {
      val command = cmd.split(' ').toSeq
      os.proc(command).call(cwd = dir, check = false, stderr = os.Pipe)
    }

    for (cmd <- build) {
      if(cmd != "") result = executeCommand(cmd)
    }
    for (cmd <- run) {
      if(cmd != "") result = executeCommand(cmd)
    }
    result
  }

  //TODO A function that takes a mapping of variables and replaces
  // the variables in the file with their values
  def mainProc(files: Seq[String], projRoot: Path, variables: Array[Map[String, String]], build: Seq[String], run: Seq[String]) = {
    val root = projRoot / ".asteroid"
    var i = 1
      for(combination <- variables) {
        for(file <- files) {
          val content = os.read(projRoot / file)
          val newContent = Template.templater(content, combination)
          os.write.over(root / file, newContent)
          val result = buildAndRun(root, build, run)
          if(result.exitCode == 0) 
            println(s"${Color.Green(s"Success [Attempt (${i})]:")}\n${result.out.text()}")
          else
            println(s"${Color.Red(s"Failure [Attempt (${i})]:")}\n\"\"\"\n${result.err.text()}\"\"\"")
        }
        i = i + 1
      }
  }

  def getCombinations(wordlists: Map[String,Array[String]]) = {

    def getCombinationsWorker(arrayOfArraysOfTuples: Array[Array[(String, String)]]): Array[Array[(String, String)]] = {

      if(arrayOfArraysOfTuples.isEmpty) {
        Array(Array.empty[(String, String)])
      } else {
        val first = arrayOfArraysOfTuples.head
        val remainingCombinations = getCombinationsWorker(arrayOfArraysOfTuples.tail)
        for{
          tuple <- first
          combination <- remainingCombinations
          } yield {
          tuple +: combination
        }
      }

    }

    val arrayOfArraysOfTuples: Array[Array[(String, String)]] = 
      wordlists.toArray.map { case (key, valueArray) =>
          valueArray.map(value => (key, value))
      }

    getCombinationsWorker(arrayOfArraysOfTuples).map(x => x.toMap)

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
