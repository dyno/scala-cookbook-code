SHELL = /bin/bash

UNAME := $(shell uname -s)
ifeq ($(UNAME),Linux)
COURSIER_POSTFIX := linux
else ifeq ($(UNAME),Darwin)
COURSIER_POSTFIX := macos
else
$(error $(UNAME) is not supported.)
endif

OPT_DIR := $(HOME)/opt

# ## Spark ##
SPARK_RELEASE := spark-3.1.1-bin-without-hadoop
export SPARK_RELEASE
$(info SPARK_RELEASE=$(SPARK_RELEASE))
SPARK_MIRROR := https://mirrors.gigenet.com/apache

SPARK_VERSION := $(shell awk -v FS=- '{print $$2;}' <<<$(SPARK_RELEASE))
$(info SPARK_VERSION=$(SPARK_VERSION))

export SPARK_HOME := $(OPT_DIR)/$(SPARK_RELEASE)
$(info SPARK_HOME=$(SPARK_HOME))

USE_AMMONITE_SPARK := 1

# ## Hadoop ##
HADOOP_RELEASE := hadoop-3.2.2
export HADOOP_RELEASE
$(info HADOOP_RELEASE=$(HADOOP_RELEASE))
HADOOP_MIRROR := https://apache.osuosl.org

# https://api.sdkman.io/2/candidates/java/Darwin/versions/list?installed=
JAVA_VERSION := 8.292.10.1-amzn
$(info JAVA_VERSION=$(JAVA_VERSION))

export HADOOP_HOME := $(OPT_DIR)/$(HADOOP_RELEASE)
$(info HADOOP_HOME=$(HADOOP_HOME))

export PATH := $(shell pwd)/bin:$(SPARK_HOME)/bin:$(HADOOP_HOME)/bin:$(PATH)
export SPARK_DIST_CLASSPATH := $(shell $(HADOOP_HOME)/bin/hadoop classpath)
$(info SPARK_DIST_CLASSPATH=$(SPARK_DIST_CLASSPATH))

.DEFAULT_GOAL: amm
.PHONY: amm-spark
amm-spark: bin/amm bin/coursier $(SPARK_RELEASE)
	# XXX: cache will make load.module behave differently.
	rm -rf ~/.ammonite/cache
	source ~/.sdkman/bin/sdkman-init.sh                \
	  && source bin/bigdata-env.sh                     \
	  && USE_AMMONITE_SPARK=$(USE_AMMONITE_SPARK) amm  \
	      --no-home-predef --predef libs/predefRepl.sc \
	# END

.PHONY: amm
amm: bin/amm bin/coursier $(SPARK_RELEASE)
	source ~/.sdkman/bin/sdkman-init.sh \
	  && amm                            \
	# END

FORCE:
%.sc: FORCE
	source ~/.sdkman/bin/sdkman-init.sh  \
	  && source bin/bigdata-env.sh       \
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
	source ~/.sdkman/bin/sdkman-init.sh             \
	  && sdk selfupdate force                       \
	  && (sdk install java $(JAVA_VERSION) || true) \
	# END

.PHONY: install-sdkman
install-sdkman:
	@echo "-- install [sdkman](https://sdkman.io/install)"
	@# XXX: sdkman is a shell function, and can not be initialized in make env.
	@if [[ ! -d ~/.sdkman ]]; then            \
	  curl -s "https://get.sdkman.io" | bash; \
	fi                                        \
	# END


$(SPARK_RELEASE): | $(SPARK_HOME)
$(SPARK_HOME): $(OPT_DIR)/$(SPARK_RELEASE).tgz
	mkdir -p $(SPARK_HOME)  && cd $(SPARK_HOME)/.. \
	  && tar xvf $(SPARK_RELEASE).tgz              \
	# END
$(OPT_DIR)/$(SPARK_RELEASE).tgz:
	mkdir -p $(SPARK_HOME)
	cd $(SPARK_HOME)/.. && curl -LO $(SPARK_MIRROR)/spark/spark-$(SPARK_VERSION)/$(SPARK_RELEASE).tgz


$(HADOOP_RELEASE): | $(HADOOP_HOME)
$(HADOOP_HOME): $(OPT_DIR)/$(HADOOP_RELEASE).tar.gz
	  mkdir -p $(HADOOP_HOME)  && cd $(HADOOP_HOME)/.. \
	  && tar xvf $(HADOOP_RELEASE).tar.gz              \
	# END
$(OPT_DIR)/$(HADOOP_RELEASE).tar.gz:
	mkdir -p $(HOADOOP_HOME)
	cd $(HADOOP_HOME)/.. && curl -LO $(HADOOP_MIRROR)/hadoop/common/$(HADOOP_RELEASE)/$(HADOOP_RELEASE).tar.gz


# 1583456966205_DATA.csv is from https://ww2.amstat.org/censusatschool/RandomSampleForm.cfm
parquet/_SUCCESS: 1583456966205_DATA.csv
	$(MAKE) schoolCensusData.sc

.PHONY: data
data:parquet/_SUCCESS
