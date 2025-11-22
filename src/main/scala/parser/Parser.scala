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

end parseUtils


// Defining the grammar used to parse the yaml config file
type Scalar = String | Int
type Mapping[A] = Map[Scalar, Node[A]]
type Node[A] = Scalar | Mapping[A] | List[A]

// A yaml document is simply a list of nodes
// Complex yaml data structures like lists of lists or lists of mappings are supported
// because they do not serve the use case, but mappings of mappings are
case class Yaml(nodesList: List[Node[Any]], filename: String) {
  
  // Receives lines of a block and returns all lines that have the same as 
  // or higher indentation than the first string of the array
  def parseBlock(block: String): Array[String] = {
    def countIndent(str: String) = for chr <- str if chr == ' ' do i=i+1; i
    val lines: Array[String] = block.split("\n")
    val firstIndent = countIndent(lines(0))
    parseUtils.filterByIndent(lines, firstIndent)
  }

  def parseIntScalar(str: String): Option[Int] = {
    try
      Some(Integer.parseInt(str.trim))
    catch
      case e: Exception => None
  }

  def parseScalar(str: String): Option[Scalar] = {
    if (parseIntScalar(str) == None) {
      Some(str)
    } else {
      parseIntScalar(str)
    }
  }

  def parseMapping(str: String): Option[Mapping[A]] = {}
  // Receives a yaml block and tries to parse it into a list
  def parseList(str: String): Option[List[A]] = {
    
  }
  def parseNode(str: String): Option[Node[A]] = {}
 }
