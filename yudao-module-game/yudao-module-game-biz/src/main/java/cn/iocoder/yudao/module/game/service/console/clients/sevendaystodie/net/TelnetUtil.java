package cn.iocoder.yudao.module.game.service.console.clients.sevendaystodie.net;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.apache.commons.net.telnet.TelnetClient;

public class TelnetUtil {

    private TelnetClient telnet = new TelnetClient();

    private InputStream in;

    private PrintStream out;

    private char prompt = '$';// 普通用户结束

//    private String user;// 用户

    private String password;// 密码

    public static void main(String[] args) {
        TelnetUtil telnetUtil = new TelnetUtil();
        telnetUtil.telnetMain("119.96.194.176", 9081, "QWER6789");

        String log = telnetUtil.sendCommand("lp");
        System.out.println("log:"+log);

    }

    /**
     * 主方法运行呢
     *
     * @param ip
     * @param port
     * @param password
     * @since: 1.0.0
     * @return:null
     * @author:SuWenyu
     * @date:2021/9/9 16:51
     */
    public void telnetMain(String ip, int port, String password) {
        try {
            this.password = password;
            telnet.connect(ip, port);
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());

            login(password);//登录方法

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     *
     * @param password
     * @since: 1.0.0
     * @return:void
     * @author:SuWenyu
     * @date:2021/8/27 9:51
     */
    public void login(String password) {
//其实这个还是要看你连接的终端，请求输入用户名和密码的标识字符是什么，别写错，不然无法登录
//        readUntil("login:");//“localhost login:“输入用户名的起点字符
//        write(user);
        readUntil("password:");//"Password:"起点字符
        write(password);//密码不会显示
        readUntil("Logon successful."); // 读取登录成功的标记
        readUntil("Press 'exit' to end session."); // 读取欢迎语标记
    }

    /**
     * 读取分析结果
     *
     * @param pattern
     * @since: 1.0.0
     * @return:String
     * @author:SuWenyu
     * @date:2021/8/27 9:52
     */
    public String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            //给字符转码，使输出的中文不乱码
            InputStreamReader br = new InputStreamReader(in, "utf-8");

            char ch = (char) br.read();
            while (true) {
                System.out.print(ch);//放在这里才能显示完整命令
                sb.append(ch);
                if (ch == lastChar) {//匹配到结束标识时返回结果
                    if (sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                }
                ch = (char) br.read();
//                System.out.print(ch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送命令
     *
     * @param value
     * @since: 1.0.0
     * @return:void
     * @author:SuWenyu
     * @date:2021/8/27 9:52
     */
    public void write(String value) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向目标发送命令字符串，并返回执行结果
     * 问题：因为结束标识符为#，所以使用命令你”cat“查看文件无法显示包含"#"的文件内容
     *
     * @param command
     * @since: 1.0.0
     * @return:String
     * @author:SuWenyu
     * @date:2021/8/27 9:52
     */
    public String sendCommand(String command) {
        System.out.println(command);
        try {
            write(command);//输入命令
            if (command == "sudo su") {//进入root模式，linux虚拟机
                readUntil("密码：");
                write(password);//重新输入密码
                this.prompt = '#';//重新定义标识符，root模式下终止标识符变为”#“
            }
            return readUntil(prompt + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭连接
     *
     * @param
     * @since: 1.0.0
     * @return:void
     * @author:SuWenyu
     * @date:2021/8/27 9:53
     */
    public void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文本数据按行读取返回字符串数据
     *
     * @param file
     * @since: 1.0.0
     * @return:String
     * @author:SuWenyu
     * @date:2021/8/27 9:53
     */
    public static String txt2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行

                if (s.length() > 60) {//这里是数据切割，使能适应输出到面板上
//判断每一行的字符数，若超过限制则转行录入，保证后期输出到面板上滚动条能滚到底
                    String s1 = s.substring(0, 60);//切开两部分分别成行
                    String s2 = s.substring(61);
                    result.append(System.lineSeparator() + s1);
                    result.append(System.lineSeparator() + s2);
                } else {
                    result.append(System.lineSeparator() + s);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
