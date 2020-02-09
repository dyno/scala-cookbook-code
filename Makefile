SHELL = /bin/bash

export PATH := $(shell pwd)/bin:$(PATH)

SPARK_SCALA_VERSION := 2.12
export SPARK_SCALA_VERSION

.DEFAULT_GOAL := amm

pdfminer.six:
	pip install pdfminer.six

toc.xml:
	dumppdf.py --extract-toc tmp/Scala-Cookbook.pdf | tee toc.xml

update-toc: toc.xml
	amm ReplaceToc.sc


.PHONY: amm
amm: bin/coursier
	source ~/.sdkman/bin/sdkman-init.sh         \
	&& amm  --no-home-predef --predef predef.sc \
	# END

bin/coursier:
	@echo "-- install [coursier](https://get-coursier.io/docs/cli-overview.html#installation)"
	mkdir -p bin
	curl -L -o bin/coursier https://git.io/coursier
	chmod +x bin/coursier

# https://api.sdkman.io/2/candidates/java/Darwin/versions/list?installed=
JAVA_VERSION := 8.0.232-amzn

.PHONY: install-java
install-java: install-sdkman
	@echo "-- install java/scala with sdkman"
	@# https://sdkman.io/usage#config
	sed -i .bak-$$(date +'%Y%m%d-%H%M%S') 's/sdkman_auto_answer=false/sdkman_auto_answer=true/' ~/.sdkman/etc/config
	source ~/.sdkman/bin/sdkman-init.sh               \
	  && sdk selfupdate force                         \
	  && (sdk install java $(JAVA_VERSION) || true)   \
	# END

.PHONY: install-sdkman
install-sdkman: ~/.sdkman
~/.sdkman:
	@echo "-- install [sdkman](https://sdkman.io/install)"
	@# XXX: sdkman is a shell function, and can not be initialized in make env.
	@if [[ ! -d ~/.sdkman ]]; then            \
	  curl -s "https://get.sdkman.io" | bash; \
	fi                                        \
	# END

# https://alvinalexander.com/scala/sbt-how-specify-main-method-class-to-run-in-project

.PHONY: sbt-run
sbt-run:
	sbt run

.PHONY: sbt-run-main
sbt-run-main:
	sbt "runMain hello.Hello"

scalatra-init:
	# http://scalatra.org/getting-started/first-project.html
	sbt new scalatra/scalatra.g8

post-stock:
	http localhost:8080/stocks/saveJsonStock symbol=AAPL price:=120

# 15.12. Setting URL Headers When Sending a Request
HttpTea:
	# http://httptea.sourceforge.net/
	java -jar HttpTea.jar -l 9001

# XXX: https://github.com/playframework/playframework/issues/4884
# `rm -rf ~/.ivy2/cache` may help to resolve depdency issue
play-init:
	sbt new playframework/play-scala-seed.g8
