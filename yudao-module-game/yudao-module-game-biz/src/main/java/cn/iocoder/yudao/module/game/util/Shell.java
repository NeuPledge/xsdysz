package cn.iocoder.yudao.module.game.util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class Shell {

    public static void main(String[] args) {
//        String cmd = "ls -al";
//        Shell shell = new Shell("101.89.205.227", "root", null);
//        String execLog = shell.execCommandPrvKey(cmd);
//        System.out.println(execLog);

        String cmd = "mkdir -p /root/.ssh/ && touch /root/.ssh/authorized_keys && echo 'ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQCmt/lNgX8TJKbu1NO9dSOVs6+5rGhMBDyfbvaJlof5rAivv6V4G7MXXHK7nwgrmoITV94BfJNT57FjEz9rSKWMev04xKCzZ+MZcJd2y2Cl+F/axZzM48SS1jPyjrdGnvlFiN6L+Cd2Rzrh75/Y/A/BCmgR90k/lzzDeo1Hrq/XhEF8maRy9c1uLawUfe/sHNGm9FY0Nlh0SC0yb8riCy1CXNr9Ci3LYaNHJnsWspC+4NhzzDG2Cdr2gPt15Rtld4GbjFwt+oRoqJpJXDjxMLYIxyWupVrE1B1rmzSXs208ig5f94+7SEYY47wQ9bknujQqItUbNkQRH1S7JMA9agyv3MDLA26yx5Yw23rxKL8OK4Ia+PKe/8WKN9dpA0n2AdZwKtN5DFTLYL/qBBtwDiNrxum++SGRLfTBAO/uBDk2kyTVe8x/M7cppriFxZeK0KfImfSwtAcSy6saQY7Hlnn6dVwQcwJcud1qi4OfE2WmOVaW8iMuPuQxMwM3z5Nc8HU= pandacloudgame' >> /root/.ssh/authorized_keys && chmod 700 /root/.ssh && chmod 600 /root/.ssh/authorized_keys";
        Shell shell = new Shell("43.143.92.61", "root", 22, "Sunchen176&401_1");
        String execLog = shell.execCommand(cmd);
        System.out.println(execLog);
    }

    private String host;
    private String username;
    private String password;
    private int port = 22;
    private int timeout = 60 * 60 * 1000;

    public Shell(String host, String username, String password, int port, int timeout) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.timeout = timeout;
    }

//    @Deprecated
//    public Shell(String host, String username) {
//        this.host = host;
//        this.username = username;
//    }

    public Shell(String host, String username, Integer port) {
        this.host = host;
        this.username = username;
        this.port = port;
    }

//    public Shell(String host, String username, String password) {
//        this.host = host;
//        this.username = username;
//        this.password = password;
//    }

    public Shell(String host, String username, Integer port, String password) {
        this.host = host;
        this.username = username;
        this.port = port;
        this.password = password;
    }

    /**
     * 公私钥认证.
     */
    public String execCommandPrvKey(String cmd) {

        JSch jSch = new JSch();
        Session session = null;
        ChannelExec channelExec = null;
        BufferedReader inputStreamReader = null;
        BufferedReader errInputStreamReader = null;
        StringBuilder runLog = new StringBuilder("");
        StringBuilder errLog = new StringBuilder("");
        try {
            // 1. 获取 ssh session
            File file = ResourceUtils.getFile("classpath:prvkey/id_rsa");
            jSch.addIdentity(file.getAbsolutePath());
            session = jSch.getSession(username, host);
            session.setTimeout(timeout);
            session.setPort(port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();  // 获取到 ssh session
            // 2. 通过 exec 方式执行 shell 命令
            channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(cmd);
            channelExec.connect();  // 执行命令
            // 3. 获取标准输入流
            inputStreamReader = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
            // 4. 获取标准错误输入流
            errInputStreamReader = new BufferedReader(new InputStreamReader(channelExec.getErrStream()));
            // 5. 记录命令执行 log
            String line = null;
            while ((line = inputStreamReader.readLine()) != null) {
                runLog.append(line).append("\n");
            }
            // 6. 记录命令执行错误 log
            String errLine = null;
            while ((errLine = errInputStreamReader.readLine()) != null) {
                errLog.append(errLine).append("\n");
            }
            // 7. 输出 shell 命令执行日志
//            log.info("exitStatus=" + channelExec.getExitStatus() + ", openChannel.isClosed=" + channelExec.isClosed());
//            log.info("命令执行完成，执行日志如下:" + runLog.toString());
            if (channelExec.getExitStatus() != 0) {
                log.error("命令执行退出值非0，错误日志如下:" + errLog);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (errInputStreamReader != null) {
                    errInputStreamReader.close();
                }
                if (channelExec != null) {
                    channelExec.disconnect();
                }
                if (session != null) {
                    session.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return runLog.toString();
    }

    /**
     * 密码认证的方式.
     */
    public String execCommand(String cmd) {
        JSch jSch = new JSch();
        Session session = null;
        ChannelExec channelExec = null;
        BufferedReader inputStreamReader = null;
        BufferedReader errInputStreamReader = null;
        StringBuilder runLog = new StringBuilder("");
        StringBuilder errLog = new StringBuilder("");
        try {
            // 1. 获取 ssh session
            session = jSch.getSession(username, host);
            if (password != null) {
                session.setPassword(password);
            }
            session.setPort(port);
            session.setTimeout(timeout);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();  // 获取到 ssh session
            // 2. 通过 exec 方式执行 shell 命令
            channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(cmd);
            channelExec.connect();  // 执行命令
            // 3. 获取标准输入流
            inputStreamReader = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
            // 4. 获取标准错误输入流
            errInputStreamReader = new BufferedReader(new InputStreamReader(channelExec.getErrStream()));
            // 5. 记录命令执行 log
            String line = null;
            while ((line = inputStreamReader.readLine()) != null) {
                runLog.append(line).append("\n");
            }
            // 6. 记录命令执行错误 log
            String errLine = null;
            while ((errLine = errInputStreamReader.readLine()) != null) {
                errLog.append(errLine).append("\n");
            }
            // 7. 输出 shell 命令执行日志
            log.info("exitStatus=" + channelExec.getExitStatus() + ", openChannel.isClosed=" + channelExec.isClosed());
            log.info("命令执行完成，执行日志如下:" + runLog.toString());
            if (channelExec.getExitStatus() != 0) {
                log.error("命令执行退出值非0，错误日志如下:" + errLog);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (errInputStreamReader != null) {
                    errInputStreamReader.close();
                }
                if (channelExec != null) {
                    channelExec.disconnect();
                }
                if (session != null) {
                    session.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return runLog.toString();
    }
}
