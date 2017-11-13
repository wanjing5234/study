#!/bin/sh
############################################################
# vesion:  1.0
# Date:    2016/12/02
# Author:  wj
# Email:   285477597@qq.com    
# Description:  mongodb backup
############################################################

#import env settings
source ./common/env_settings.sh

#mongodb back up
function mongodb_backup(){
/usr/bin/mongodump -h${MONGODB_HOST} -port${MONGODB_PORT} -d ${MONGODB_DB_NAME} -o ${MONGODB_BACKUP_DIR} > /dev/null
}

#mongodb restore
function mongodb_restore(){
	/usr/bin/mongorestore -h${MONGODB_HOST} -port${MONGODB_PORT} -d ${MONGODB_DB_NAME} --directoryperdb ${MONGODB_BACKUP_DIR}/${MONGODB_DB_NAME}
}

#mongodb select
function mongodb_select(){
	/usr/bin/mongorestore -h${MONGODB_HOST} -port${MONGODB_PORT} -d ${MONGODB_DB_NAME} --directoryperdb ${MONGODB_BACKUP_DIR}/${MONGODB_DB_NAME}
}

#main
function mongodb_main(){
	if [[ $1 == "backup" ]]; then
		mongodb_backup
	elif [[ $1 == "restore" ]]; then
		mongodb_restore
	else 
    	echo "USAGE: [backup | restore]"
	fi
}

#####main start
#echo -e "-----mongodb数据库备份开始${DATETIME}-----" >> $LOG_FILE

mongodb_main $1

#echo -e "-----mongodb数据库备份结束${DATETIME}-----" >> $LOG_FILE
