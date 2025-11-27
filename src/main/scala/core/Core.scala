package core
import os.Path
object Prepare {
  // Receives the path to the root of the project
  // which is the directory of asteroid.belt
  def replicateDirectory(projectRoot: Path) = {
    def copyAny(path: Path) = {
      val name = path.getSegment(path.segments.length-1)
      os.copy.over(path, projectRoot / ".asteroid" / name)
      }
    val homedir = os.home
    os.makeDir.all(projectRoot / ".asteroid")
    val paths = os.list(projectRoot).filter(p => p != (projectRoot / ".asteroid") && p != (projectRoot / "asteroid.belt"))
    paths.map(p => {copyAny(p)})
  }

  def clean(projectRoot: String) = {
    
  }
}
