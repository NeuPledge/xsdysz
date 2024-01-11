package cn.iocoder.yudao.module.game.service.console.clients.sevendaystodie;

import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.module.game.util.OKHttpUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class SevenDaysToDieClient {

    private static final SevenDaysToDieClient client = new SevenDaysToDieClient();

    private SevenDaysToDieClient() {
    }

    public static SevenDaysToDieClient getInstance() {
        return client;
    }

    private String assemblyUrl(InstanceDO instance) {
        return String.format("http://%s:%d/command", instance.getIp(), instance.getAgentPort());
    }

    /**
     * 登录 并 返回token.
     *
     * @return token.
     */
    public String getToken(InstanceDO instance) {
        Map<String, String> param = new HashMap<>();
        // type: admin:管理员, block:黑名单, white:白名单
        param.put("command", "login");
        param.put("password", "QWER6789");
        String response = OKHttpUtil.sendPost(assemblyUrl(instance), param);
        if (StringUtils.isEmpty(response)) {
            return null;
        }
        if (response.contains("Login Failed")) {
            return null;
        }
        if (response.contains("Logged In")) {
            String[] split = response.split("\\^");
            try {
                String token = split[0];
                return token;
            } catch (IndexOutOfBoundsException exception) {
                return null;
            }
        }
        return response;
    }

    // 列出在线玩家
    public String listPlayers(InstanceDO instance) {
        String token = getToken(instance);
        Map<String, String> param = new HashMap<>();
        // type: admin:管理员, block:黑名单, white:白名单
        param.put("command", "serverCommand");
        param.put("serverCommand", "lp");
        param.put("token", token);
        String response = OKHttpUtil.sendPost(assemblyUrl(instance), param);
        if (StringUtils.isEmpty(response)) {
            return null;
        }
        return response;
    }

    public static void main(String[] args) {
        SevenDaysToDieClient client1 = SevenDaysToDieClient.getInstance();
        InstanceDO instance = new InstanceDO();
        instance.setIp("119.96.194.176");
        instance.setAgentPort(9080);
        String token = client1.listPlayers(instance);
        System.out.println(token);
    }

}
