package parser

// A few utility methods to help with parsing
object parseUtils:
  // Remove spaces from a string
  def clean(str: String) = str.filter(x => x != ' ')
  // Removes all lines that don't start with **at least** n indentations
  def filterByIndent(str: String, n: Int) = {
    val lines: Array[String] = str.split("\n")
    val space: String = " "
    str.filter(x => x.startsWith(space*n))
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
