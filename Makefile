SHELL = /bin/bash

.DEFAULT_GOAL := amm
amm:
	$(MAKE) -C ammonites amm

pdfminer.six:
	pip install pdfminer.six

toc.xml:
	dumppdf.py --extract-toc tmp/Scala-Cookbook.pdf | tee toc.xml

update-toc: toc.xml
	amm ReplaceToc.sc

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
