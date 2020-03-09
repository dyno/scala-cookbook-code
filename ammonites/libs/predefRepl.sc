import $exec.`predefShared`

ammonite.shell.Configure(interp, repl, wd)
interp.configureCompiler(_.settings.nowarn.value = false)
