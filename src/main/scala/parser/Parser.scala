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

  // Removes all lines starting with a '#' from a string
  def removeComments(str: String) = {
    var lines = str.split("\n")
    lines = lines.filter(x => !(x.startsWith("#"))).map(y => y ++ "\n")
    lines.mkString
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
type Node[A] = Scalar | Mapping[A] | Array[Scalar]

// 
object yamlParser {
  
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

  // Receives a string, for each line on the same indentation as the
  // first line, takes the string before the column as the key then
  // sends the rest (whether a block or a value right after the column)
  // to the list and scalar parsers to attach their result to the value,
  // if both fail then returns None
  def parseMapping(str: String): Option[Mapping[Any]] = {
    def countIndent(str: String) = for chr <- str if chr == ' ' do i=i+1; i
    val lines = str.split("\n")
    val filteredLines = parseBlock(lines)   
    var finalMapping: Mapping[Any] = Map()
    var i = 0
    for line <- filteredLines
      if(countIndent(line) == countIndent(filteredLine(0)))
    do
      line = parseUtils.clean(line)
      var lineParts = line.split(":")
      if(lineParts.size == 1) {
        val valueBlock = filteredLines.slice(i+1, filteredLines.size-1)
        // Try passing the block to the list and mapping parsers
        parseList(valueBlock.mkString) match {
          case Some(list) => finalMapping += (lineParts(0), list)
          case None => parseMapping(valueBlock.mkString) match {
            case Some(mapping) => finalMapping += (lineParts(0), mapping)
            case None => None
          }
        }
      } else {
        parseScalar(lineParts(1)) match {
          case Some(value) => finalMapping += (lineParts(0), value)
          case None => None
        }
      }
      i = i + 1
    Some(finalMapping)
  }

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
