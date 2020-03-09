// repl internal state
pprint.pprintln(interp.repositories())
pprint.pprintln(repl.sess.frames(0).classpath)
pprint.pprintln(repl.fullImports)

// log
pprint.log(scala.util.Properties.versionNumberString)
pprint.log(s"${1 + 1}")

// desugar
import $ivy.`io.get-coursier::coursier:2.0.0-RC6-10`
import coursier._
desugar(dep"sh.almond::ammonite-spark:0.90")

