#!/bin/bash
############################################################
# vesion:  1.0
# Date:    2016/12/05
# Author:  wj
# Email:   285477597@qq.com    
# Description:  env settings 
############################################################

#date
DATE=`(date +%Y-%m-%d)`
DATETIME=`(date "+%Y-%m-%d %H:%M:%S")`

#version info
SOURCE_VERSION=1.2.0.0
TARGET_VERSION=1.3.0.0

#current dir
CURRENT_DIR=$PWD
BASE_DIR=${CURRENT_DIR%/*}

#file dir info
SRC_DIR=${BASE_DIR}/src
LIB_DIR=${BASE_DIR}/lib
LOGS_DIR=${BASE_DIR}/log
LOG_FILE=${LOGS_DIR}/$DATE.log

ACTION_DIR=${SRC_DIR}/action

#create folder
if [ ! -d ${LOGS_DIR} ]; then
	mkdir -p ${LOGS_DIR}
fi

mkdir -p ${BASE_DIR}/{src,log}

#cmd
CMD_BASH="/bin/bash"
CMD_LOG="/bin/bash ./util/logs_util.sh"

#mysql info
MYSQL_HOST=127.0.0.1
MYSQL_PORT=3306
MYSQL_USERNAME=root
MYSQL_PASSWORD=cloudfort
MYSQL_DB_BOXSAFE=boxsafe

MYSQL_DB_FLEXSAFE=flexsafe

MYSQL_CMD="/usr/bin/mysql -h${MYSQL_HOST} -u${MYSQL_USERNAME} -p${MYSQL_PASSWORD}"
MYSQL_CMD_FLEXSAFE="$MYSQL_CMD -D${MYSQL_DB_FLEXSAFE} -s -N"

MYSQLDUMP_CMD="/usr/bin/mysqldump -h${MYSQL_HOST} -P${MYSQL_PORT} -u${MYSQL_USERNAME} -p${MYSQL_PASSWORD}"

#---------------Define--------------  
ECHO="echo -ne" 
ESC="\033[" 

#--------------Variable--------------  
#ANSI ESC action  
TRUE=1
FALSE=0 
REV=7 
 
#color  
NULL=0 
BLACK=30 
RED=31 
GREEN=32 
ORANGE=33 
BLUE=34 
PURPLE=35 
SBLUE=36 
GREY=37 