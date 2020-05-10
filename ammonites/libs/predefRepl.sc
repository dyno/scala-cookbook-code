import $exec.`predefShared`

ammonite.shell.Configure(interp, repl, wd)

// https://docs.scala-lang.org/overviews/compiler-options/index.html
interp.configureCompiler(_.settings.nowarn.value = false)
interp.configureCompiler(_.settings.deprecation.value = true)
