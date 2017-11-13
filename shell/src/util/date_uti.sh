#!/bin/sh
############################################################
# vesion:  1.0
# Date:    2017/01/16
# Author:  wj
# Email:   285477597@qq.com    
# Description:  date util
############################################################

function getDate(){
	echo `(date +%Y-%m-%d)`
}

function getDateTime(){
	echo `(date "+%Y-%m-%d %H:%M:%S")`
}

getDate
getDateTime