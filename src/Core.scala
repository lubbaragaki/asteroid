package core
//import os.Path
//import os._
//import fansi._
import collection.mutable.StringBuilder

//> using dep com.lihaoyi::os-lib:0.11.6
//> using dep org.luaj:luaj-jse:3.0.1
//> using dep com.lihaoyi::fansi:0.5.1
//> using file "./Lua.scala"

import os.*
import fansi.*
import loader.Loader
import lua.Lua

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

  // A function that takes a mapping of variables and replaces
  // the variables in the file with their values
  def mainProc(files: Seq[String], projRoot: Path, variables: Array[Map[String, String]], build: Seq[String], run: Seq[String], script: Option[String], noOutput: Boolean) = {
    val root = projRoot / ".asteroid"
    var i = 0
    var succ = 0
      for(combination <- variables) {
        i = i + 1
        for(file <- files) {
          val filePath = os.RelPath(file)
          if(!os.exists(root / filePath)) {
            println(s"'${file}' file not found:\nMake sure the files mentioned in your yaml configuration start at the root of the project (where 'asteroid.bel' is placed)")
            Loader.clean(root)
            sys.exit(1)
          }
          val content = os.read(projRoot / filePath)
          val newContent = Template.templater(content, combination)
          os.write.over(root / filePath, newContent)
        }
        println("----------------------")
        val result = buildAndRun(root, build, run)
        //if script exists -> outcode = runscript
        val outcode: Int = script match {
          case Some(scriptname) => Lua.runScript(scriptname, result.out.text()).toint()
          case None => result.exitCode
        }
        if(outcode == 0) 
          println(s"${Color.Green(s"Success [Attempt ${i}]:")}")
          println(s"${Color.Yellow("Input: ")}${combination}")
          if(noOutput != true) {
            println(s"${Color.Yellow("Output: ")}${result.out.text()}")
          }
          succ = succ + 1
        else
          println(s"${Color.Red(s"Failure [Attempt ${i}]:")}")
          println(s"${Color.Yellow("Input: ")}${combination}")
          if(noOutput != true) {
            println(s"${Color.Yellow("Output: ")}\n\"\"\"\n${result.err.text()}${result.out.text()}\n\"\"\"")
          }
      }
      println("======================")
      val finRes = if(succ == i) then Color.Green(s"${succ}/${i}") else Color.Red(s"${succ}/${i}")
      println(s"${Color.Yellow("Results: ")}${finRes}")
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
