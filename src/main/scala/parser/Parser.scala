package parser

// A few utility methods to help with parsing
object parseUtils:

  // Remove spaces from a string
  def clean(str: String) = str.filter(x => x != ' ')
  // Removes all lines that don't start with **at least** n indentations
  def filterByIndent(lines: Array[String], n: Int) = {
    val space: String = " "
    lines.filter(x => x.startsWith(space*n))
  }

  // Remove leading dashes (for yaml list element) and leading white spaces (indentation)
  def removeLeadingChars(str: String) = str.filter(s => s.startsWith(space*2)).map(c => c.filter(c => c != '-' && c != ' '))
  def extractString(str: String) = {
    if(str.startsWith("\"") && str.endsWith("\""))
      str.slice(1, str.size-1)
    else
      str
  }

end parseUtils


// Defining the grammar used to parse the yaml config file
type Scalar = String | Int
type Mapping[A] = Map[Scalar, Node[A]]
type Node[A] = Scalar | Mapping[A] | Array[A]

//
case class Yaml() {
  
  // Receives lines of a block and returns all lines that have the same as 
  // or higher indentation than the first string of the array
  def parseBlock(lines: Array[String]): Array[String] = {
    def countIndent(str: String) = for chr <- str if chr == ' ' do i=i+1; i
    val firstIndent = countIndent(lines(0))
    parseUtils.filterByIndent(lines, firstIndent)
  }

  // Handles parsing int errors
  def parseIntScalar(str: String): Option[Int] = {
    try
      Some(Integer.parseInt(str.trim))
    catch
      case e: Exception => None
  }
  
  // Receives a string and returns the same string or an int
  // or None if it failed to parse both
  def parseScalar(str: String): Option[Scalar] = {
    if (parseIntScalar(str) == None) {
      Some(str)
    } else {
      parseIntScalar(str)
    }
  }

  def parseMapping(str: String): Option[Mapping[Any]] = {}

  // Receives a yaml block and tries to parse it into a list
  // If it is not a valid yaml list, returns None, otherwise returns
  // the Array wrapped in a monad. If a list element is not a valid
  // scalar, it is marked as undefined and will be skipped
  def parseList(str: String): Option[Array[Any]] = {
    def isValidList(lines: Array[String]) = {
      var verif = true
      for line <- lines if (!line.startsWith("-")) do verif = true
      verif
    }
    val lines = str.split("\n")
    val filteredLines = parseBlock(lines)   
    if (!isValidList(filteredLines))
      None
    var finalList: Array[Any] = Array()
    for line <- filteredLines
    do
      parseScalar(parseUtils.removeLeadingChars(line)) match {
        case Some(scalar) => finalList = finalList ++ Array(scalar)
        case None => finalList = finalList ++ Array("Undefined node type")
      }
    Some(finalList)
  }
 }
