#!/bin/bash
############################################################
# vesion:  1.0
# Date:    2017/11/07
# Author:  wj
# Email:   285477597@qq.com
# Description:  test/test.sh
############################################################

#import env settings
source ./common/env_settings.sh

declare -i arg1
declare -i arg2
declare -i arg3

function test() {
    echo "-----> 11111 $0 ${args1}"
    echo "-----> 22222 $0 ${args2}"
    echo "-----> 33333 $0 ${args3}"
}

function main() {
    ${CMD_LOG} "writeMsg" $0 "start" "主程序开始执行"

    args1=`echo $1 | cut -d \, -f 1`
    args2=`echo $1 | cut -d \, -f 2`
    args3=`echo $1 | cut -d \, -f 3`

    test

    ${CMD_LOG} "writeMsg" $0 "end" "主程序执行结束"
}

#####main start
main $1

