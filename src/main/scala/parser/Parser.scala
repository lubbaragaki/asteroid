package parser

// Defining the grammar used to parse the yaml config file

type Filepath = String
def isFilepath(str: String) = {
  path: Filepath
  if (str.startsWith("./")) {
    val path = str.asInstanceOf[Filepath]
  } else {
    str
  }
}
type Scalar = String | Int | Filepath
type Mapping[A] = Map[Scalar, A]
type Node[A] = Scalar | Mapping[A] | List[A]

// A yaml document is simply a list of nodes
case class Yaml(nodesList: List[Node], filename: String) {
  def parseStringScalar(str: String): Option[String | Filepath]
  def parseIntScalar(str: String): Option[Int]
  def parseScalar(str: String): Option[Scalar]
  def parseMapping(str: String): Option[Mapping[A]]
  def parseList(str: String): Option[List[A]]
  def parseNode(str: String): Option[List[Node[A]]]
  // Intent: return a list of 1 element which is then added to
  // the object's list of nodes using the ++ operator, returning
  // a new list since 'List's in Scala are immutable
  // If parseNode returns 'None' then there is an error in the file
 }
