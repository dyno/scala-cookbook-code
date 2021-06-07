// https://github.com/alexarchambault/ammonite-spark#compatibility
import pprint.log
import coursierapi.{Dependency, ScalaVersion}

val depOfAmmoniteSpark = Dependency.parse(
  // > 0.10.1 seems have problem with
  // "java.lang.BootstrapMethodError: java.lang.NoClassDefFoundError: ammonite/util/Frame"
  "sh.almond::ammonite-spark:0.10.1",
  ScalaVersion.of(scala.util.Properties.versionNumberString)
)
log(interp.load.ivy(depOfAmmoniteSpark))
