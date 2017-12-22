package com.kk.study.framework.sshxcute;

import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

public class SshxcuteTest {

    public static void main(String[] args) throws TaskExecFailException {

        // 新建一个 ConnBean 对象，三个参数依次是 ip 地址、用户名、密码
        ConnBean cb = new ConnBean("127.0.0.1", "root","A");

        // 将上面新建的 ConnBean 作为参数传递给 SSHExec 的静态单例方法，得到一个 SSHExec 的实例
        SSHExec ssh = SSHExec.getInstance(cb);

        // 利用上面得到的 SSHExec 实例连接主机
        ssh.connect();

        CustomTask sampleTask = new ExecCommand("echo '----------> 123'");

        Result res = ssh.exec(sampleTask);
        if (res.isSuccess) {
            System.out.println("Return code: " + res.rc);
            System.out.println("sysout: " + res.sysout);
        } else {
            System.out.println("Return code: " + res.rc);
            System.out.println("error message: " + res.error_msg);
        }


        // 断开远程主机
        ssh.disconnect();

    }

}
