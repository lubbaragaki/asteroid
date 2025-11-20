package parser

// Defining the grammar used to parse the yaml config file
type Scalar = String | Int
type Mapping[A] = Map[Scalar, Node[A]]
type Node[A] = Scalar | Mapping[A] | List[A]

// A yaml document is simply a list of nodes
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
  def parseList(str: String): Option[List[A]] = {}
  def parseNode(str: String): Option[Node[A]] = {}
 }
