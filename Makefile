SHELL = /bin/bash

pdfminer.six:
	pip install pdfminer.six

toc.xml:
	dumppdf.py --extract-toc tmp/Scala-Cookbook.pdf | tee toc.xml

update-toc: toc.xml
	amm ReplaceToc.sc
