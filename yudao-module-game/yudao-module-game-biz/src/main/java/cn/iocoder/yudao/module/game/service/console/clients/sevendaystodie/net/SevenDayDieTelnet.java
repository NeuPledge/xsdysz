package cn.iocoder.yudao.module.game.service.console.clients.sevendaystodie.net;

import org.apache.commons.net.telnet.TelnetClient;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SevenDayDieTelnet {
    private TelnetClient telnetClient = null;
    private InputStream is = null;
    private OutputStream os = null;

    /**
     * 连接远程计算机,连接完成后，获取读取流与发送流
     *
     * @param ip   远程计算机IP地址
     * @param port 远程计算机端口
     */
    public void connection(String ip, int port) {
        try {
            telnetClient = new TelnetClient();
            telnetClient.connect(ip, port);
            is = telnetClient.getInputStream();
            os = telnetClient.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取远程计算机返回的信息
     */
    public String readTelnetMsg() {
        try {
            int len = 0;
            byte[] b = new byte[1024];
            do {
                len = is.read(b);
                if (len >= 0)
                    return new String(b, 0, len);
            }
            while (len >= 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向远端计算机发送指令消息
     *
     * @param msg 需要传送的指令
     **/
    public void sendTelnetMsg(String msg) {
        byte[] b = msg.getBytes();
        try {
            os.write(b, 0, b.length);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找远端计算机返回的指令中是否包含想要指令
     * 一直查找，直到包含，返回true
     */
    public boolean findStr(String str) {
        for (; ; ) {
            String msg = readTelnetMsg();
            if(msg==null) return false;
            if (msg.indexOf(str) != -1)
                return true;
        }
    }

    /**
     * 关闭连接，关闭IO
     */
    public void close() {
        try {
            is.close();
            os.close();
            telnetClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

