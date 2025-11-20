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

case class Yaml()


object Parser {

}
