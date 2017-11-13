#!/bin/bash
############################################################
# vesion:  1.0
# Date:    2016/12/02
# Author:  wj
# Email:   285477597@qq.com    
# Description:  mysql backup
############################################################

source ./common/env_settings.sh

#mysql create db
function mysql_create_db(){
	${MYSQL_CMD} < ${SHELL_FILES}/flexsafe.sql 2>> /dev/null
}

#mysql drop db
function mysql_drop_db(){
	MYSQL_DROP_DB_SQL="DROP database IF EXISTS ${MYSQL_DB_FLEXSAFE}"
	echo ${MYSQL_DROP_DB_SQL} | ${MYSQL_CMD}
}

#mysql back up
function mysql_backup(){
	${MYSQLDUMP_CMD} -B ${MYSQL_DB_BOXSAFE} > ${MYSQL_BACKUP_DIR}/${MYSQL_DB_BOXSAFE}.sql 2>> /dev/null
	${MYSQLDUMP_CMD} -B ${MYSQL_DB_FLEXSAFE} > ${MYSQL_BACKUP_DIR}/${MYSQL_DB_FLEXSAFE}.sql 2>> /dev/null
	
	${MYSQLDUMP_CMD} ${MYSQL_DB_FLEXSAFE} ${MYSQL_TB_FLEXSAFE_BACKUP_CONFIG} > ${MYSQL_BACKUP_DIR}/${MYSQL_TB_FLEXSAFE_BACKUP_CONFIG}.sql 2>> /dev/null
	${MYSQLDUMP_CMD} ${MYSQL_DB_FLEXSAFE} ${MYSQL_TB_FLEXSAFE_BACKUP_ITEM} > ${MYSQL_BACKUP_DIR}/${MYSQL_TB_FLEXSAFE_BACKUP_ITEM}.sql 2>> /dev/null
}

#mysql restore
function mysql_restore(){
	MYSQL_CREATE_DB_SQL="CREATE DATABASE IF NOT EXISTS $MYSQL_DB_BOXSAFE DEFAULT CHARSET utf8 COLLATE utf8_general_ci;"
	#/usr/bin/mysql -h$MYSQL_HOST -u$MYSQL_USERNAME -p$MYSQL_PASSWORD $MYSQL_CMD_CREATE_DB
	
	echo ${MYSQL_CREATE_DB_SQL} | ${MYSQL_CMD}

	${MYSQL_CMD} $MYSQL_DB_BOXSAFE < $MYSQL_BACKUP_DIR/$MYSQL_DB_BOXSAFE.sql
}

#mysql table exist
function mysql_table_exist(){
	table_name=$1
	sql="SELECT TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='${MYSQL_DB_FLEXSAFE}' AND TABLE_NAME='${table_name}'"
	result=`echo "${sql}" | ${MYSQL_CMD} | grep -v "TABLE_NAME"`
	if [ "${result}" = "${table_name}" ] ; then
		echo "-----exist-----"
	else
		echo "-----not exist-----"
	fi
}

#mysql rebuild table
function mysql_rebuild_table(){
	sql="rename table ${MYSQL_TB_FLEXSAFE_BACKUP_CONFIG} to ${MYSQL_TB_FLEXSAFE_BACKUP_CONFIG}_old;"
	echo ${sql} | ${MYSQL_CMD_FLEXSAFE}
	
	sql="rename table ${MYSQL_TB_FLEXSAFE_BACKUP_ITEM} to ${MYSQL_TB_FLEXSAFE_BACKUP_ITEM}_old;"
	echo ${sql} | ${MYSQL_CMD_FLEXSAFE}
	
	mysql -uroot -p${MYSQL_PASSWD} < ${SHELL_FILES}/flexsafe.sql
}

#main
function mysql_main(){
	if [[ $1 == "createDB" ]]; then
		mysql_create_db
	elif [[ $1 == "dropDB" ]]; then
		mysql_drop_db
	elif [[ $1 == "backup" ]]; then
		mysql_backup
	elif [[ $1 == "restore" ]]; then
		mysql_restore
	elif [[ $1 == "rebuildTable" ]]; then
		mysql_rebuild_table
	elif [[ $1 == "tableExist" ]]; then
		mysql_table_exist
	else 
    	echo "USAGE: [backup | restore]"
	fi
}

#####main start
#echo -e "-----Mysql数据库备份开始${DATETIME}-----" >> $LOG_FILE

mysql_main $1

#echo -e "-----Mysql数据库备份结束${DATETIME}-----" >> $LOG_FILE
