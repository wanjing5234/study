#!/usr/bin/env bash

if [[ -f /etc/profile.d/flexsafe_proj_home.sh ]]; then
	source /etc/profile.d/flexsafe_proj_home.sh
fi

BK_USER="cfbackup"
BK_ROOT="/home/cfbackup"
LOG_ON="1"

# SYSTEM_ROOT_DIR="/flexsafe/data"
SYSTEM_ROOT_DIR="/flexsafe"
COPY_FILES="/boxsafe/data /etc/flexsafe.conf /flexsafe_userdata /var/log"

MYSQL_HOST=127.0.0.1
MYSQL_PORT=3306
MYSQL_USERNAME=root
MYSQL_PASSWORD=${MYSQL_PASSWD}
MYSQL_DB_BOXSAFE=boxsafe
MYSQL_DB_FLEXSAFE=flexsafe
MYSQL_TB_ITEM=backup_item

MYSQL_CMD="mysql -h${MYSQL_HOST} -u${MYSQL_USERNAME} -p${MYSQL_PASSWORD}"
MYSQL_CMD_FLEXSAFE="$MYSQL_CMD -D${MYSQL_DB_FLEXSAFE} -s -N"

MYSQLDUMP_CMD="/usr/bin/mysqldump -x -h$MYSQL_HOST -P$MYSQL_PORT -u$MYSQL_USERNAME -p$MYSQL_PASSWORD"

OSS_URL="oss://oss-cn-hangzhou.aliyuncs.com/"

if [ $OS_SYS == "centos" ]; then
	os_apache_user="apache"
	DUPLICITY_CMD="/usr/bin/duplicity"
else
	os_apache_user="www-data"
	DUPLICITY_CMD="/usr/local/bin/duplicity"
fi

createLogFile()
{
	if [[ ! -d /var/log/flexsafe-backup ]]; then
		mkdir -p /var/log/flexsafe-backup
	fi

	if [[ ${mode} == "b" ]]; then
		if [[ -z $BACKUP_LOG_FILE ]]; then
			LOG_FILE="/var/backup-${itemId}.log"
		else
			LOG_FILE=$BACKUP_LOG_FILE
		fi
	elif [[ ${mode} == "r" ]]; then
		if [[ -z $RESTORE_LOG_FILE ]]; then
			LOG_FILE="/var/restore-${itemId}.log"
		else
			LOG_FILE=$RESTORE_LOG_FILE
		fi
	else
		LOG_FILE="/var/log/flexsafe-backup/tmp.log"
		cat /dev/null > ${LOG_FILE}
	fi
	
	if [[ ! -f ${LOG_FILE} ]]; then
		touch ${LOG_FILE}
	fi
	
	if [[ ! -f /var/log/flexsafe-backup/restore.log ]]; then
		touch /var/log/flexsafe-backup/restore.log
	fi
}

log()
{
	if [[ -z $LOG_ON ]]; then
		return
	fi
	
	_LogInfo=$1
	msg="[ "`date +20'%y-%m-%d %H:%M:%S'`" ]"" ${_LogInfo} "
	echo "$msg"
	echo "$msg" >> $LOG_FILE 2>&1
}

runCmd()
{
	log "cmd: $cmd"
	output=`$cmd 2>&1`
	exitValue=$?
	echo $output | while read line
	do
		log "$line"
	done
}

createParentDir()
{
	pdir=`dirname $1`
	if [[ ! -d $pdir ]]; then
		mkdir -p $pdir
	fi
}

end_time()
{
	end_time=`date +%s%N | cut -c1-13 `
	cost_time_ms=$((${end_time}-${begin_time}))
	log "Running time ${cost_time_ms}ms"
}

readConfig()
{
	log "Read config."
	sql="SELECT bc.server_ip, bc.backup_server_type, bi.reserved_cnt, bi.incremental_cnt, bi.type, bi.src_id, bc.access_id, bc.access_key, bc.uuid FROM backup_config bc LEFT JOIN backup_item bi ON bi.backup_config_id = bc.id WHERE bi.id = ${itemId};"
	config=`$MYSQL_CMD_FLEXSAFE -e "$sql" | tail -n 1 `
	if [[ -z $config ]]; then
		log "No backup config, exit."
		exit 1
	fi
	serverIp=`echo $config | awk -F ' ' '{print $1}'`
	backupServerType=`echo $config | awk -F ' ' '{print $2}'`
	reservedCnt=`echo $config | awk -F ' ' '{print $3}'`
	incrementalCnt=`echo $config | awk -F ' ' '{print $4}'`
	itemType=`echo $config | awk -F ' ' '{print $5}'`
	srcId=`echo $config | awk -F ' ' '{print $6}'`
	accessId=`echo $config | awk -F ' ' '{print $7}'`
	accessKey=`echo $config | awk -F ' ' '{print $8}'`
	uuid=`echo $config | awk -F ' ' '{print $9}'`
}

readSystemConfig()
{
	sc_sql="SELECT value FROM system_config WHERE NAME  = 'global.system.uuid';"
	sc_config=`$MYSQL_CMD_FLEXSAFE -e "${sc_sql}" | tail -n 1 `
	if [[ -z ${sc_config} ]]; then
		log "No system config, exit."
		exit 1
	fi
	sc_uuid=`echo ${sc_config} | awk -F ' ' '{print $1}'`
}

parseItem()
{
	log "Parse Item begin."
	case $itemType in
		"0")
			# Volume
			itemType="VOLUME"
			srcDir=`df --output=target $srcId | tail -n 1`
			lvName=`lvs -o lv_full_name --noheadings $srcId | tail -n 1 | sed -e "s/^[ \s]\{1,\}//g" | sed -e "s/[ \s]\{1,\}$//g"`
		;;	
		"1")
			# User
			itemType="USER"
			getUserSql="select root_directory from user where id=$srcId;"
			srcDir=`$MYSQL_CMD_FLEXSAFE -e "$getUserSql" | tail -n 1` 
		;;
		"2")
			itemType="SYSTEM"
			srcDir=$SYSTEM_ROOT_DIR
		;;
	esac

	destDir="$BK_ROOT/$itemType$srcDir"
	destDirTmp="$BK_ROOT/$itemType$srcDir"	

	log "ItemId : ${itemId}, itemType: ${itemType}, srcId: ${srcId}"
	log "DestDir : ${destDir}"
	log "Parse Item end."
}

initData()
{
	if [ 0 == ${backupServerType} ]; then
		serverType="local"
		destDir="rsync://$BK_USER@$serverIp/${destDirTmp}"
	elif [ 1 == ${backupServerType} ]; then
		serverType="aliyun"
		bucket_name="cloudfort-backup8-${uuid}-${itemId}"
		destDir="${OSS_URL}${bucket_name}"
	elif [ 2 == ${backupServerType} ]; then
		serverType="aws"
		bucket_name="cloudfort-backup8-${uuid}-${itemId}"
		folder_name=${itemId}
		destDir="${serverIp}/${bucket_name}/${folder_name}"
	fi
}

################################################
#backup begin
backup() 
{
	log "Start backup to ${serverType} by ${origin}."
	
	#testConn
	testConn
	conn_status=$?
	if [[ ${conn_status} != 0 ]];then 
		log "Backup end itemId = ${itemId}, status:failed, not connection server."
		log "------------------------------------------------------"
		exit 127
	fi

	backupItem
	
	end_time

	log "Backup end itemId = ${itemId}, status:finished."
	
	log "------------------------------------------------------"
	
	cat ${LOG_FILE} >> /var/log/flexsafe-backup/backup.log
}

backupItem()
{
	log "Backup Item begin."
	if [[ -d $srcDir ]]; then
		if [[ $itemType == "SYSTEM" ]]; then
			dumpData $srcDir
		fi
		
		if [ 0 == ${backupServerType} ]; then
			create_remote_dir
		fi
		
		get_backup_mode
		
		statistic_file_info
		
		backup_to_server
		
		delete_oldest_versions
	else
		log "SrcDir [$srcDir] is not a directory"
	fi
	log "Backup Item end."
}

create_remote_dir()
{
	rcmd="ls ${destDirTmp}"
	remoteCmd
	if [[ $? != 0 ]]; then
		log "Create remote dir ${destDirTmp}"
		rcmd="mkdir -p ${destDirTmp}"
		remoteCmd
		if [[ $? == 0 ]]; then
			log "Create remote dir ${destDirTmp} success."
		else
			log "Create remote dir ${destDirTmp} failed."
		fi
	fi
}

statistic_file_info() 
{
	count=`ls -lR ${srcDir} | wc -l`
	size=`du -csh ${srcDir} | tail -1 | awk '{print $1}'`
	log "Backup ${srcDir} file count = ${count}, file size = ${size}."
	
	
}

backup_to_server()
{
	log "Backup to ${serverType} ${srcDir} -> ${destDir}"
	
	command=`${DUPLICITY_CMD} ${backup_mode} ${srcDir} ${destDir}`
	result=$?
	if [[ ${result} != 0 ]]; then
		log "Backup end itemId = ${itemId}, status:failed, backup to server failed."
		log "------------------------------------------------------"
		exit 127
	fi
}

#备份模式，全量备份，增量备份
get_backup_mode() 
{
	
	#查找最后一次匹配的行号 tail -1
	count=`${DUPLICITY_CMD} collection-status ${destDir} | grep -nE 'Full*' | tail -1 | awk -F ':' '{print $1}'`

	if [[ ${count} == "" ||	${count} == 0 ]]; then
		backup_mode="full"
	else
		incremental_count=`${DUPLICITY_CMD} collection-status ${destDir} | tail -n +${count} | grep -nE 'Incremental*' | wc -l`
		if [[ ${incremental_count} -ge ${incrementalCnt} ]];then
			backup_mode="full"
		else
			backup_mode="incr"
		fi
	fi

	log "Backup mode ${backup_mode}"		

	echo ${backup_mode}
}

dumpData()
{
	BASE_DIR=$1
	BACKUP_DIR=$BASE_DIR/.backup
	rm -rf $BACKUP_DIR
	MYSQL_BACKUP_DIR=$BACKUP_DIR/mysql_backup
	mkdir -p $MYSQL_BACKUP_DIR
	
	log "Dump boxsafe database."
	${MYSQLDUMP_CMD} -B $MYSQL_DB_BOXSAFE > $MYSQL_BACKUP_DIR/$MYSQL_DB_BOXSAFE.sql 
	
	log "Dump flexsafe database."
	${MYSQLDUMP_CMD} -B $MYSQL_DB_FLEXSAFE > $MYSQL_BACKUP_DIR/$MYSQL_DB_FLEXSAFE.sql
	
	log "Copy data files."
	for target in $COPY_FILES
	do
		createParentDir ${BACKUP_DIR}$target
		if [[ -d ${BACKUP_DIR}$target ]]; then
			rm -rf ${BACKUP_DIR}$target
		fi
		cp -aR --remove-destination $target ${BACKUP_DIR}$target
	done
}

delete_oldest_versions()
{
	log "Delete oldest extra backup versions"
	olderThen=$(($reservedCnt)) 
	command=`${DUPLICITY_CMD} remove-all-but-n-full ${olderThen} --force ${destDir}`
	result=$?
	if [[ ${result} != 0 ]]; then
		log "Backup end itemId = ${itemId}, status:failed, delete oldest extra backup versions failed."
		log "------------------------------------------------------"
		exit 127
	fi
}

#backup end
################################################

################################################
#set env begin 

set_env_export()
{
	if [ 0 == ${backupServerType} ]; then
		set_local_export
	elif [ 1 == ${backupServerType} ]; then
		set_aliyun_export
	elif [ 2 == ${backupServerType} ]; then
		set_aws_export
	fi
}

unset_env_export()
{
	if [ 0 == ${backupServerType} ]; then
		unset_local_export
	elif [ 1 == ${backupServerType} ]; then
		unset_aliyun_export
	elif [ 2 == ${backupServerType} ]; then
		unset_aws_export
	fi
}

set_local_export()
{
	export PASSPHRASE="EEF3A9AF"
}

unset_local_export()
{
	unset PASSPHRASE
}


set_aliyun_export()
{
	export PASSPHRASE="100100"
	export AliCLD_OSS_HOST=${serverIp}
	export AliCLD_ACCESS_ID=${accessId}
	export AliCLD_ACCESS_KEY=${accessKey}
}

unset_aliyun_export()
{
	unset PASSPHRASE
	unset AliCLD_OSS_HOST
	unset AliCLD_ACCESS_ID
	unset AliCLD_ACCESS_KEY		
}

set_aws_export()
{
	export PASSPHRASE="EEF3A9AF"
	export AWS_ACCESS_KEY_ID=${accessId}
	export AWS_SECRET_ACCESS_KEY=${accessKey}
}

unset_aws_export()
{
	unset PASSPHRASE
	unset AWS_ACCESS_KEY_ID
	unset AWS_SECRET_ACCESS_KEY
}

#set env end
################################################

################################################
#list versions begin 

listVersions()
{
	#log "[listVersions]"
	
	log "list versions ${serverType}."	
		
	${DUPLICITY_CMD} collection-status ${destDir} | grep -E 'Full*|Incremental*' | awk '{for(i=1;i<=NF-1;i++)printf $i" ";printf "\n"}'
	
	exit $?
}

#list versions end 
################################################

################################################
#restore begin

restore()
{
	log "Start restore by ${origin}."
	
	if [[ -z $restoreDes ]]; then
		restoreDes=$srcDir
	fi
	
	log "${destDir} -> $restoreDes"
	
	parent=`dirname $restoreDes`
	if [[ ! -e $parent ]]; then
		mkdir -p ${parent}
	fi

	files_trashbin_handle

	#log "[restore]"
	restore_from_server

	if [[ $itemType == "SYSTEM" && $restoreDes == $srcDir ]]; then
		restoreData $srcDir
	fi

	if [[ $itemType == "SYSTEM" ]]; then
		flush_crontab
	fi

	if [[ $itemType != "SYSTEM" ]]; then
		log "Register file info to mysql by ${serverType}."
		python /usr/local/flexsafe/sys_backup/main.py $itemId
	fi

	updateUserSize

	end_time

	log "Restore end itemId = ${itemId}, status:finished."	
	
	log "------------------------------------------------------"
	
	cat ${LOG_FILE} >> /var/log/flexsafe-backup/restore.log
	
	exit $exitValue
}

restore_from_server()
{	
	log "Restore ${serverType} ${bkVersionToRestore} ${restoreDes}."
	
	rm -rf ${SYSTEM_ROOT_DIR}/.backup/

	echo "-----> ${DUPLICITY_CMD} restore --restore-time ${bkVersionToRestore} $force ${destDir} $restoreDes"

	command=`${DUPLICITY_CMD} restore --restore-time ${bkVersionToRestore} $force ${destDir} $restoreDes > /dev/null`
	result=$?
	if [[ ${result} != 0 ]]; then
		log "Restore end itemId = ${itemId}, status:failed, restore from server failed."
		exit 127
	fi
	
	if [ 1 == ${backupServerType} ]; then
		ls | grep -v "lost+found" | xargs chown -R ${os_apache_user}:${os_apache_user}
	fi
}

restoreData()
{
	BASE_DIR=$1
	BACKUP_DIR=$BASE_DIR/.backup
	MYSQL_BACKUP_DIR=$BACKUP_DIR/mysql_backup
	
	log "Restore boxsafe database."
	$MYSQL_CMD < $MYSQL_BACKUP_DIR/$MYSQL_DB_BOXSAFE.sql
	
	log "Restore flexsafe database."
	$MYSQL_CMD < $MYSQL_BACKUP_DIR/$MYSQL_DB_FLEXSAFE.sql
	
	log "Restore data files."
	for target in $COPY_FILES
	do
		if [[ -e ${BACKUP_DIR}$target ]]; then
			if [[ -d $target ]]; then
				rm -rf $target
			fi
			cp -aR --remove-destination ${BACKUP_DIR}$target $target > /dev/null
		fi
	done
}

files_trashbin_handle()
{
	log "Restore Delete files trashbin begin."
	
	sql="select root_directory from user where deleted = 0 "
	if [[ $itemType == "VOLUME" ]]; then
		sql="${sql} and lv_name like '${srcId}%';"
	elif [[ $itemType == "USER" ]]; then
		sql="${sql} and id = ${srcId};"
	elif [[ $itemType == "SYSTEM" ]]; then
		sql="${sql} ;"	
	fi
	
	userList=`$MYSQL_CMD_FLEXSAFE -e "$sql"`
	for user in ${userList}                                                                                                   
	do 
    	root_directory=${user}
    	rm -rf ${root_directory}/files_trashbin/files/*
	done
	
	log "Restore Delete files trashbin end."
}

flush_crontab() 
{
	log "Restore Flush crontab begin."
	
	cat /dev/null > /tmp/cron
	sql="select id, backup_interval, backup_time from ${MYSQL_TB_ITEM} where status = 1; "
	while read -a row
	do
		id=${row[0]}
		backup_interval=${row[1]}
		backup_time=${row[2]}
			
		hour=`echo ${backup_time} | awk -F ':' '{print $1}'`
		minute=`echo ${backup_time} | awk -F ':' '{print $2}'`
    		
		exec_statement="${minute} ${hour} */${backup_interval} * * flexsafe_backup -oi ${id}"
		echo "${exec_statement}" >> /tmp/cron
		crontab /tmp/cron
	done< <(echo ${sql} | ${MYSQL_CMD_FLEXSAFE})
	
	log "Restore Flush crontab end."
}

updateUserSize()
{
	log "Update user size begin."

	sql="select id from user where deleted = 0 "
	if [[ $itemType == "VOLUME" ]]; then
		sql="${sql} and lv_name like '${srcId}%';"
	elif [[ $itemType == "USER" ]]; then
		sql="${sql} and id = ${srcId};"
	elif [[ $itemType == "SYSTEM" ]]; then
		sql="${sql} ;"
	fi

    userIdStr=
	userList=`${MYSQL_CMD_FLEXSAFE} -e "$sql"`
	for userId in ${userList}
	do
        userIdStr+="${userId},"
	done
	userIdStr=${userIdStr%,*}
    update_user_size_sql="update user set used_size = 1 where id in (${userIdStr})"
    `${MYSQL_CMD_FLEXSAFE} -e "${update_user_size_sql}"`

	log "Update user size end."
}

#restore end
################################################

testConn()
{
	conn_status=-1
	if [ 0 == ${backupServerType} ]; then
		testConn_local
		conn_status=$?
	else
		testConn_yun
		conn_status=$?
	fi
	return ${conn_status}
}

testConn_local()
{
	log "Test connection ${serverIp}."
echo "----->11111 ssh $BK_USER@$serverIp hostname"
	command=`timeout 30 ssh $BK_USER@$serverIp hostname`
	result=$?
	if [[ ${result} != 0 ]]; then
		log "Connection ${serverIp} timeout, exitCode = ${result}."
	else
		log "Connection ${serverIp} success."
	fi
		
	return ${result}
}

testConn_yun()
{
	log "Test connection ${serverType} ${serverIp}."

	if [ 1 == ${backupServerType} ]; then
		test_bucket_name="cloudfort-backup-test-${sc_uuid}"
		test_destDir="${OSS_URL}${test_bucket_name}"
	elif [ 2 == ${backupServerType} ]; then
		test_bucket_name="cloudfort-backup-test-${sc_uuid}"
		test_destDir="${serverIp}/${test_bucket_name}"
	fi
	
	command=`timeout 30 ${DUPLICITY_CMD} collection-status ${test_destDir} > /dev/null 2>&1` 
	
	result=$?
	if [[ ${result} != 0 ]]; then
		log "Connection ${serverType} timeout, exitCode = ${result}."
	else
		log "Connection ${serverType} success."
	fi
		
	return ${result}
}

testConn_server_ip()
{
	log "Test connection ${serverIp}."
	command=`timeout 30 ssh $BK_USER@$serverIp hostname`
	result=$?
	if [[ ${result} != 0 ]]; then
		log "Connection ${serverIp} timeout, exitCode = ${result}."
	else
		log "Connection ${serverIp} success."
	fi
		
	exit ${result}
}

remoteCmd()
{
	#LOG_ON="" # close log
	ssh -l $BK_USER $serverIp "$rcmd"
}

usage()
{
	echo "Usage: `basename $0` [-l]/[-r]/[-p cmd]/[-t serverIp] [-f] [-i backupItemId] [-v bkVersionToRestore] [-d destinationPath]"
	exit 10
}

# We have 4 modes:
# b: backup mode, backup all backup items stored in database. (default)
# l: list mode, list all backup items or list all backup versions of one backup item.
# r: restore mode, restore one backup item.
# t: test mode, test connection to server.
mode="b"
bkVersionToRestore="now"
force=""
serverIp=""
itemId=""
restoreDes=""
rcmd=""
origin="manual" #manual, task
while getopts :lroft:p:i:d:v: OPTION
do
	case $OPTION in
		l)
			mode="l"
			;;
		r)
			mode="r"
			;;
		t)
			mode="t"
			serverIp=$OPTARG
			;;
		p)
			mode="p"
			rcmd=$OPTARG
			;;
		o)
			origin="task"
			;;
		f)
			force="--force"
			;;
		i)
			itemId=$OPTARG
			;;
		v)
			bkVersionToRestore=$OPTARG
			;;
		d)
			restoreDes=$OPTARG
			;;
		\?)
			usage
			;;
		esac
done

#create log file
createLogFile

begin_time=`date +%s%N | cut -c1-13`

if [[ ${origin} == "task" ]]; then
	count=$(ps -ef | grep -v grep | grep 'flexsafe_backup -i '${itemId}'' | wc -l)
	if [[ ${count} -gt 0 ]]; then
		log "Backup itemId = ${itemId} has been executed. The crontab timed task is cancelled."
		exit 0
	fi

fi

case $mode in
	b)
		if [[ -f /var/backup-${itemId}.log ]]; then
			cat /dev/null > /var/backup-${itemId}.log
		fi
		log "Backup itemId = ${itemId} begin"
	;;
	r)
		if [[ -f /var/restore-${itemId}.log ]]; then
			cat /dev/null > /var/restore-${itemId}.log
		fi
		log "Restore itemId = ${itemId} begin"
		;;
esac

#read config
if [[ $mode != "t" ]]; then
	
	rm -rf /root/.cache/duplicity/*
	
	readConfig

	readSystemConfig
	
	parseItem
	
	initData
	
	set_env_export
fi

case $mode in
	b)
		# backup all items
		backup
		;;
	l)
		# list backup versions
		listVersions
		;;
	r)
		# restore backup
		restore
		;;
	t)
		# test connection to server
		testConn_server_ip
		;;
	p)
		remoteCmd
		;;
esac

unset_env_export


