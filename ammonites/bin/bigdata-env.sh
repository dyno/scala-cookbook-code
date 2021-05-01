#!/usr/bin/env bash

# =============================================================================
# Setup environment variables for pyspark tests.
#
# Usage: source bigdata-env.sh
#
# =============================================================================

GIT_ROOT=$(git rev-parse --show-toplevel 2>/dev/null)
CONF_ROOT=${GIT_ROOT}/ammonites/conf
HOME_OPT=${HOME_OPT:-${HOME}/opt}

# ## Hadoop ##

HADOOP_RELEASE=${HADOOP_RELEASE:=hadoop-3.2.2}
HADOOP_HOME=${HOME_OPT}/${HADOOP_RELEASE}
[[ -d "${HADOOP_HOME}" ]] || echo "HADOOP_HOME=${HADOOP_HOME} does not exists! please run 'make ${HADOOP_RELEASE}'."
HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
if ! grep -q ":${HADOOP_HOME}/bin:" <<<${PATH}; then
  PATH=${HADOOP_HOME}/bin:${PATH}
fi

# leverage environment provided authentication mechanism
core_site_file=${CONF_ROOT}/core-site.xml
[[ -f "${core_site_file}" ]] && cp -f "${core_site_file}" ${HADOOP_HOME}/etc/hadoop || true

export HADOOP_HOME HADOOP_CONF_DIR

# ## Spark ##

SPARK_RELEASE=${SPARK_RELEASE:=spark-3.1.1-bin-without-hadoop}
SPARK_VERSION=$(awk -v FS=- '{print $2;}' <<<${SPARK_RELEASE})
# http://spark.apache.org/docs/latest/hadoop-provided.html
SPARK_HOME=${HOME_OPT}/${SPARK_RELEASE}
[[ -d "${SPARK_HOME}" ]] || echo "SPARK_HOME=${SPARK_HOME} does not exists! please run 'make ${SPARK_RELEASE}'."
SPARK_CONF_DIR=${SPARK_HOME}/conf
SPARK_CONF_DEFAULTS=${SPARK_CONF_DIR}/spark-defaults.conf
if ! grep -q ":${SPARK_HOME}/bin:" <<<${PATH}; then
  PATH=${SPARK_HOME}/bin:${PATH}
fi

# configure spark executor memory etc
spark_defaults_file=${CONF_ROOT}/spark-defaults.conf
[[ -f "${spark_defaults_file}" ]] && cp -f "${spark_defaults_file}" ${SPARK_CONF_DIR} || true
# for local spark history server.
mkdir -p /tmp/spark-events/

# enable spark to use hadoop/hdfs to access s3
# https://hadoop.apache.org/docs/current/hadoop-aws/tools/hadoop-aws/index.html
SPARK_DIST_CLASSPATH=$(hadoop --config ${HADOOP_CONF_DIR} classpath)
if ! grep -q aws <<<${SPARK_DIST_CLASSPATH}; then
  # https://hadoop.apache.org/docs/r3.1.1/hadoop-project-dist/hadoop-common/UnixShellAPI.html
  ADD_HADOOP_AWS_LIB="hadoop_add_to_classpath_tools hadoop-aws"
  if [[ ! -f ~/.hadooprc ]] || ! (grep -qF "${ADD_HADOOP_AWS_LIB}" ~/.hadooprc); then
    echo ${ADD_HADOOP_AWS_LIB} >>~/.hadooprc
  fi
  SPARK_DIST_CLASSPATH=$(hadoop --config ${HADOOP_CONF_DIR} classpath)
fi

export SPARK_HOME SPARK_CONF_DIR SPARK_DIST_CLASSPATH SPARK_CONF_DEFAULTS
