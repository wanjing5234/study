#!/bin/sh
############################################################
# vesion:  1.0
# Date:    2016/12/02
# Author:  wj
# Email:   285477597@qq.com    
# Description:  mongodb backup
############################################################

source ./common/env_settings.sh

#print logs
function printLogs() {
	cmd=$1
	opt=$2
	state=$3
	date=`date +20'%y-%m-%d %H:%M:%S'`
	if [ $state == 0 ];then
		echo -e "\n${date}  ${cmd}  ${opt} "
	else
		echo -e "\n${date}  ${cmd}  ${opt} "
	fi
}

#write opt into logs
function writeStateIntoLogs() {
	cmd=$1
	opt=$2
	state=$3
	date=`date +20'%y-%m-%d %H:%M:%S'`
	if [ $state == 0 ];then
		echo -e "${date}  ${cmd}  ${opt} is success" >> ${LOG_FILE}
	else
		echo -e "${date}  ${cmd}  ${opt} is failed" >> ${LOG_FILE}
	fi
}

#write opt into logs
function writeMsgIntoLogs() {
	cmd=$1
	opt=$2
	msg=$3
	date=`date +20'%y-%m-%d %H:%M:%S'`
	echo -e "${date}  ${cmd}  ${opt}  ${msg} " >> ${LOG_FILE}
}

#read logs
function readLogs(){
	echo "------------- read log -------------"
}

#main
function logs_main(){
	if [[ $1 == "write" ]]; then
		writeStateIntoLogs $2 $3 $4
	elif [[ $1 == "writeMsg" ]]; then
		writeMsgIntoLogs $2 $3 $4
	elif [[ $1 == "read" ]]; then
		readLogs
	else 
    	echo "USAGE: logs_util.sh [backup | restore]" >> $LOG_FILE
	fi
}

#####main start
#echo -e "\n-----日志开始${DATETIME}-----" >> $LOG_FILE

logs_main $1 $2 $3 $4

#echo -e "-----日志结束${DATETIME}-----\n" >> $LOG_FILE
