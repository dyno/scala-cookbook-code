import ammonite.ops._
import scala.xml.XML

def getTocMap(tocFile: String): Map[String, String] = {
  val toc = XML.loadFile(tocFile)
  val outlines = (toc \\ "outline") collect { case node if (node \@ "level") == "2" => node \@ "title" } map {
    _.replace("\n", "")
      .replaceAll("\\s+", " ")
      .replaceAll("b'(.*)'", "$1")
      .replaceAll("\\\\xe2\\\\x88\\\\x92", "-")
      .replaceAll("\\\\xe2\\\\x80\\\\x9c", "\"")
      .replaceAll("\\\\xe2\\\\x80\\\\x9d", "\"")
  }
  val pattern = raw"(\d+\.\d+). (.*)".r
  outlines.collect { case pattern(section, title) => (section, title) } toMap
}

def expandToc(p: Path, tocMap: Map[String, String]): Unit = {
  val pattern = """//\s?(\d+\.\d+)\s*$""".r
  val updated = read(p).lines
    .map {
      case pattern(section) => s"// $section ${tocMap(section)}"
      case line             => line
    }
    .mkString("\n")
  write.over(p, updated)
}

// ## main ##
val tocMap = getTocMap("toc.xml")
val scList = ls ! pwd |? (_.ext == "sc")
scList.foreach(expandToc(_, tocMap))