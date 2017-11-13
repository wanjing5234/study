#!/bin/bash
############################################################
# vesion:  1.0
# Date:    2017/01/16
# Author:  wj
# Email:   285477597@qq.com    
# Description:  menu
############################################################

#import env settings
source ./common/env_settings.sh

function initalize(){
	#每次进来如果日志存在，先把之前到日志清空，日志文件不存在，则先创建
	if [ -f ${LOG_FILE} ]; then 
		cat /dev/null > ${LOG_FILE}
	else
		touch ${LOG_FILE}
	fi
	
	#修改权限
	chmod +x ${SRC_DIR}/*.sh
}

function usage(){
	while true; do
	read -p "Please input your selected : " selected
	case "$selected" in
        	1)
                	#数据库的备份
					/bin/bash ./action/backup.sh backupDB
					exit;
        	;;
        	2)
        			#数据库的迁移，此处把数据库的迁移放到前面执行，后面直接从mysql中读取数据
                	/bin/bash ./action/db_migrate.sh
                	exit
        	;;
        	3)
        			#数据文件的备份
                	/bin/bash ./action/backup.sh backupDataFile
                	exit;
        	;;
        	4)
        			#源文件的备份
                	/bin/bash ./action/backup.sh backupSourceFile
                	exit;
        	;;
        	5)
        			#系统更新
                	/bin/bash ./action/system_upgrade.sh
                	exit
        	;;
        	6)
                	#数据清理
                	/bin/bash ./action/cleanup.sh
                	exit;
        	;;
        	7)
                	fun_exit
        	;;
        	*)
                	echo "Usage: $0 ..."
        	;;
		esac
	done
}

function showOption(){
	while :
	do
echo -e "\n"
cat<<EOF
			*****************************************************
			*                系统升级执行步骤                   *
			*                                                   *
			*   1. 数据库的备份                                 *
			*   2. 数据库的迁移                                 *
			*   3. 数据文件备份                                 *
			*   4. 源文件的备份                                 *
			*   5. 系统更新                                     *
			*   6. 数据清理                                     *
			*   7. 退出                                         *
			*                                                   *
			*****************************************************
EOF
		usage
	done
}

function fun_exit(){
	exit
}

function main() {
	showOption
}

#系统初始化
initalize

#####main start
${CMD_LOG}  "writeMsg" "main" "start" "主程序开始执行"
main
${CMD_LOG}  "writeMsg" "main" "end" "主程序执行结束"
