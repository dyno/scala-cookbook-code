/** StringInterpolation */
// https://docs.scala-lang.org/overviews/core/string-interpolation.html
// https://alvinalexander.com/scala/scala-class-object-function-convert-multiline-string-to-list-seq

object Q {
  def apply(s: String): Seq[String] =
    s.split("\n").toSeq.map(_.trim).filter(_ != "")
}

val list = Q("""
    http://angel.co/mile-high-organics
    http://angel.co/kindara
    http://angel.co/precog
    http://angel.co/pivotdesk
""")

implicit class QHelper(val sc: StringContext) {
  def Q(args: Any*): Seq[String] = {
    val strings = sc.parts.iterator.toSeq
    var buf = strings.map { s => pprint.log(s); s }.mkString("\n")
    buf.toString.split("\n").toSeq.map(_.trim).filter(_ != "")
  }
}

val list = Q"""
    http://angel.co/mile-high-organics
    http://angel.co/kindara
    http://angel.co/precog
    http://angel.co/pivotdesk
"""

implicit class JsonHelper(val sc: StringContext) extends AnyVal {
  def json(args: Any*): ujson.Value = {
    val strings = sc.parts.iterator
    val expressions = args.iterator
    var buf = new StringBuffer(strings.next)
    while (strings.hasNext) {
      buf append expressions.next
      buf append strings.next
    }

    pprint.log(buf)
    ujson.read(buf)
  }
}

val name = "dyno"
val id = "dynofu"
json"""{ "name": "$name", "id": "$id" }"""
