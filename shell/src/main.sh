#!/bin/bash
############################################################
# vesion:  1.0
# Date:    2017/11/02
# Author:  wj
# Email:   285477597@qq.com
# Description:  menu
############################################################

#import env settings
source ./common/env_settings.sh

function initalize() {
	#每次进来如果日志存在，先把之前到日志清空，日志文件不存在，则先创建
	if [ -f ${LOG_FILE} ]; then
		cat /dev/null > ${LOG_FILE}
	else
		touch ${LOG_FILE}
	fi

	#修改权限
	chmod +x ${SRC_DIR}/*.sh
}

usage() {
	echo "Usage: `basename $0` url=***?args=***"
	exit 127
}

function main() {
    ${CMD_LOG} "writeMsg" $0 "start" "主程序开始执行"

    if [ $# != 1 ] ; then
        usage
    fi

    data=$1
    ${CMD_LOG} "writeMsg" $0 "data=${data}"

    arr=(${data//\?/ })
    for i in ${arr[@]}
    do
        if echo $i | grep -qe '^url' ; then
            url=`echo $i | cut -d \= -f 2`
            absolute_url=${ACTION_DIR}${url}
        elif echo $i | grep -qe '^args' ; then
            args=`echo $i | cut -d \= -f 2`
        fi
    done

    #args1=`echo ${data} | cut -d \? -f 1`
    #args1=`echo ${data} | cut -d \? -f 2`
    #echo "-----> absolute_url=${absolute_url}"
    #echo "-----> args=${args}"

    ${CMD_BASH} ${absolute_url} ${args}

    ${CMD_LOG} "writeMsg" $0 "goto" "${absolute_url}?${args}"

    #showOption

    ${CMD_LOG} "writeMsg" $0 "end" "主程序执行结束"
}

#系统初始化
initalize

#####main start
main $1