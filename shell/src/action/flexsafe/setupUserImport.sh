#!/bin/bash

USER_DATA_FILE="/flexsafe_userdata"

MYSQL_HOST=127.0.0.1
MYSQL_PORT=3306
MYSQL_USERNAME=root
MYSQL_PASSWORD=${MYSQL_PASSWD}
MYSQL_DB_FLEXSAFE=flexsafe
MYSQL_TB_GROUPS=groups
MYSQL_TB_USER_IMPORT=user_import

MYSQL_CMD="mysql -h${MYSQL_HOST} -u${MYSQL_USERNAME} -p${MYSQL_PASSWORD}"
MYSQL_CMD_FLEXSAFE="$MYSQL_CMD -D${MYSQL_DB_FLEXSAFE} -s -N"

function createLinuxDirectory() {
    username=$1
    directoryPath="/flexsafe/${username}"
    #mkdir -p ${directoryPath}

    if [ $OS_SYS == "centos" ]; then
	    os_apache_user="apache"
    else
        os_apache_user="www-data"
    fi

    #echo "directoryPath=${directoryPath}, os_apache_user=${os_apache_user} "

    return
    chown -R ${os_apache_user}:${os_apache_user} ${directoryPath}

    chmod -R 0750 ${directoryPath}
}

function setLink() {
    username=$1;
    destpath="/flexsafe/${username}";

    cd ${BOXSAFE_BACKUP_HOME};
    #echo "ln -s $2/$1 $1" > ./tmp

    #echo "username=${username}, destpath=${destpath} "
    return;

    ln -s ${destpath} ${username};
    chown -R ${OS_APACHE_USER}:${OS_APACHE_USER} ${username}
    chown -R ${OS_APACHE_USER}:${OS_APACHE_USER} ${destpath}
    if [[ "$OS_SYS" == "centos" ]]
    then
        chcon -t httpd_sys_rw_content_t -R ${username};
        chcon -t httpd_sys_rw_content_t -R ${destpath} ;
    fi
}

function execCurl() {
    username=$1
    email=$2
    password=$3
    lv_size=$4

    url="http://cfservice:cfzoom2016@localhost/ocs/v2.php/cloud/users"

    curl -X POST ${url} -d userid=${username} -d password=${password}

    curl -X PUT ${url}/${username} -d key=email -d value=${email}

    curl -X PUT ${url}/${username} -d key=quota -d value=${lv_size}GB

    curl -X POST ${url}/${username}/groups -d groupid=${defaultGroupName}
}

function insertUserGroup() {
     ${MYSQL_CMD_FLEXSAFE} -e "insert into test.student values ("$i",'oldboy"$i"','m','21','computer"$i"');"

}

function updateUserData() {
    userDataName=$1
    userDataPassword=$2
    echo "${userDataName} = ${userDataPassword}" >> ${USER_DATA_FILE}
}

function createUser() {
    username=$1
    email=$2
    password=$3
    lvDize=$4
    userDataUsername=$5
    userDataPassword=$6

    begin_time_createLinuxDir=`date +%s%N | cut -c1-13`
    createLinuxDirectory ${username}
    end_time_createLinuxDir=`date +%s%N | cut -c1-13`
    cost_time_createLinuxDir=$((end_time_createLinuxDir - begin_time_createLinuxDir))
    echo "${username} 执行createLinuxDirectory cost time = ${cost_time_createLinuxDir}"

    begin_time_setLink=`date +%s%N | cut -c1-13`
    #setLink ${username}
    end_time_setLink=`date +%s%N | cut -c1-13`
    cost_time_setLink=$((end_time_setLink - begin_time_setLink))
    echo "${username} 执行setLink cost time = ${cost_time_setLink}"

    begin_time_execCurl=`date +%s%N | cut -c1-13`
    #execCurl ${username} ${email} ${password} ${lvDize}
    end_time_execCurl=`date +%s%N | cut -c1-13`
    cost_time_execCurl=$((end_time_execCurl - begin_time_execCurl))
    echo "${username} 执行execCurl cost time = ${cost_time_execCurl}"

    begin_time_updateUserData=`date +%s%N | cut -c1-13`
    #updateUserData ${userDataUsername} ${userDataPassword}
    end_time_updateUserData=`date +%s%N | cut -c1-13`
    cost_time_updateUserData=$((end_time_updateUserData - begin_time_updateUserData))
    echo "${username} 执行updateUserData cost time = ${cost_time_updateUserData}"

}

function getUserImportList() {
    batch=$1
    sql="SELECT username, email, password, lv_size, user_data_username, user_data_password FROM ${MYSQL_TB_USER_IMPORT} where batch = ${batch};"
    while read username email password lv_size user_data_username user_data_password
    do
        createUser ${username} ${email} ${password} ${lv_size} ${user_data_username} ${user_data_password}
    done< <(echo "${sql}" | ${MYSQL_CMD_FLEXSAFE})
}

function getDefaultGroup() {
    sql="SELECT group_name FROM ${MYSQL_TB_GROUPS} WHERE deleted = 0 and default_group = 1 LIMIT 1;"
    while read group_name
    do
        defaultGroupName=${group_name}
    done< <(echo "${sql}" | ${MYSQL_CMD_FLEXSAFE})
}

function main() {
    begin_time_getDefaultGroup=`date +%s%N | cut -c1-13`
    getDefaultGroup
    end_time_getDefaultGroup=`date +%s%N | cut -c1-13`
    cost_time_getDefaultGroup=$((end_time_getDefaultGroup - begin_time_getDefaultGroup))
    echo "执行getDefaultGroup cost time = ${cost_time_getDefaultGroup}"

    begin_time_getUserImportList=`date +%s%N | cut -c1-13`
    getUserImportList $1
    end_time_getUserImportList=`date +%s%N | cut -c1-13`
    cost_time_getUserImportList=$((end_time_getUserImportList - begin_time_getUserImportList))
    echo "执行getUserImportList cost time = ${cost_time_getUserImportList}"
}

#####main start
main $1




