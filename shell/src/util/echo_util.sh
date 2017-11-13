#!/bin/sh
############################################################
# vesion:  1.0
# Date:    2017/01/16
# Author:  wj
# Email:   285477597@qq.com    
# Description:  echo util
############################################################


#write opt into logs
function echo_msg() {
	msg=$1
	date=`date +20'%y-%m-%d %H:%M:%S'`
	if [ $state == 0 ];then
		echo -e "\n${date} - ${msg}"
	else
		echo -e "\n${date} - ${msg}"
	fi
}

#main
function echo_main(){
	if [[ $1 == "echo" ]]; then
		echo_msg $2
	elif [[ $1 == "write" ]]; then
		readLogs
	else 
    	echo "USAGE: echo_util.sh [echo | write]"
	fi
}

#####main start
echo_main $1 $2
