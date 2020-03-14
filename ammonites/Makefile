SHELL = /bin/bash

UNAME := $(shell uname -s)
ifeq ($(UNAME),Linux)
COURSIER_POSTFIX := linux
else ifeq ($(UNAME),Darwin)
COURSIER_POSTFIX := macos
else
$(error $(UNAME) is not supported.)
endif

export ENABLE_HIVE := 0
export SPARK_SCALA_212 := 1

ifeq ($(ENABLE_HIVE),1)
SPARK_RELEASE := spark-2.4.5-bin-hadoop2.7
export USE_AMMONITE_SPARK := 0
else ifeq ($(SPARK_SCALA_212),1)
SPARK_RELEASE := spark-2.4.5-bin-without-hadoop-scala-2.12
export USE_AMMONITE_SPARK := 1
else
SPARK_RELEASE := spark-2.4.5-bin-without-hadoop
export USE_AMMONITE_SPARK := 1
endif
SPARK_VERSION := $(shell awk -v FS=- '{print $$2;}' <<<$(SPARK_RELEASE))

HADOOP_RELEASE := hadoop-3.2.1
export HADOOP_RELEASE

# https://api.sdkman.io/2/candidates/java/Darwin/versions/list?installed=
JAVA_VERSION := 8.0.232-amzn

# bin/amm use SPARK_RELEASE to tell whether to start spark with scala 2.11 or 2.12
export SPARK_RELEASE
export SPARK_HOME := $(shell pwd)/$(SPARK_RELEASE)
export HADOOP_HOME := $(shell pwd)/$(HADOOP_RELEASE)

export PATH := $(shell pwd)/bin:$(SPARK_HOME)/bin:$(HADOOP_HOME)/bin:$(PATH)
export SPARK_DIST_CLASSPATH := $(shell $(HADOOP_HOME)/bin/hadoop classpath)

$(info SPARK_RELEASE=$(SPARK_RELEASE), SPARK_VERSION=$(SPARK_VERSION))
$(info HADOOP_RELEASE=$(HADOOP_RELEASE), SPARK_DIST_CLASSPATH=$(SPARK_DIST_CLASSPATH))

.DEFAULT_GOAL: amm
.PHONY: amm
amm: bin/amm bin/coursier $(SPARK_RELEASE)
	# XXX: cache will make load.module behave differently.
	rm -rf ~/.ammonite/cache
	source ~/.sdkman/bin/sdkman-init.sh                \
	  && USE_AMMONITE_SPARK=$(USE_AMMONITE_SPARK) amm  \
	      --no-home-predef --predef libs/predefRepl.sc \
	# END

FORCE:
%.sc: FORCE
	source ~/.sdkman/bin/sdkman-init.sh  \
	  && USE_AMMONITE_SPARK=0 amm        \
	    --no-home-predef                 \
	    --predef libs/predefShared.sc $@ \
	# END

.PHONY: install-ammonite
install-ammonite: install-coursier install-java

.PHONY: install-coursier
install-coursier: bin/coursier
bin/coursier:
	@echo "-- install [coursier](https://get-coursier.io/docs/cli-overview.html#installation)"
	mkdir -p bin
	curl -L -o bin/coursier https://git.io/coursier-cli-$(COURSIER_POSTFIX)
	chmod +x bin/coursier

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
install-sdkman:
	@echo "-- install [sdkman](https://sdkman.io/install)"
	@# XXX: sdkman is a shell function, and can not be initialized in make env.
	@if [[ ! -d ~/.sdkman ]]; then            \
	  curl -s "https://get.sdkman.io" | bash; \
	fi                                        \
	# END

$(SPARK_RELEASE):
	curl -LO http://mirrors.ibiblio.org/apache/spark/spark-$(SPARK_VERSION)/$(SPARK_RELEASE).tgz \
	  && tar xvf $(SPARK_RELEASE).tgz                                                            \
	# END

$(HADOOP_RELEASE):
	curl -LO http://mirror.cc.columbia.edu/pub/software/apache/hadoop/common/$(HADOOP_RELEASE)/$(HADOOP_RELEASE).tar.gz \
	  && tar xvf $(HADOOP_RELEASE).tar.gz                                                                               \
	# END

# 1583456966205_DATA.csv is from https://ww2.amstat.org/censusatschool/RandomSampleForm.cfm
parquet/_SUCCESS: 1583456966205_DATA.csv
	$(MAKE) schoolCensusData.sc

.PHONY: data
data:parquet/_SUCCESS
