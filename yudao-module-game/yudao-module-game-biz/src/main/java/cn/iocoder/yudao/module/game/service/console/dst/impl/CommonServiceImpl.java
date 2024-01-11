package cn.iocoder.yudao.module.game.service.console.dst.impl;


import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.module.game.dal.dataobject.operatelog.GameOperateLogDO;
import cn.iocoder.yudao.module.game.dal.mysql.operatelog.GameOperateLogMapper;
import cn.iocoder.yudao.module.game.framework.HeaderHolder;
import cn.iocoder.yudao.module.game.service.console.clients.dst.DstClient;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.CavesServerIni;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.MasterServerIni;
import cn.iocoder.yudao.module.game.service.console.dst.ICommonService;
import cn.iocoder.yudao.module.game.util.Shell;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommonServiceImpl implements ICommonService {
    

    @Autowired
    GameOperateLogMapper operateLogMapper;
    

    // 强制启动agent, 适用于各种agent异常无法启动，通过强制启动可以确保agent一定能启动.
    @Override
    public String forceStartAgent(InstanceDO instance) {
        String ip = instance.getIp();
        Shell shell = new Shell(ip, "root", instance.getSshPort());
        StringBuffer runLog = new StringBuffer();
        // 1. 强制关闭agent
        String pidRunningResult = shell.execCommandPrvKey("ps -ef | grep dst[a]gent | awk '{print $2}'");
        String pidRunning = pidRunningResult.trim();
        if (StringUtils.isEmpty(pidRunning)) {
            // 当前未在运行
            runLog.append("云控未在运行中\n");
        } else {
            // 在运行, 需要kill
            runLog.append("云控运行中，需要kill，pid:").append(pidRunning).append("\n");
            shell.execCommandPrvKey("kill -9 " + pidRunning);
            runLog.append("云控已kill\n");
        }

        // 2. 删除pid
        shell.execCommandPrvKey("cd /root/agents && rm -f dstagent.pid");
        runLog.append("删除dstagent.pid\n");

        // 3. 启动agent
        runLog.append("正在启动云控\n");
        startAgent(instance);
        String testLog = shell.execCommandPrvKey("curl http://127.0.0.1:18188/dst-agent/api/version");
        runLog.append("检测是否启动成功返回结果:").append(testLog);
        if (StringUtils.isEmpty(testLog) || !testLog.contains("data")) {
            // 启动失败
            runLog.append("检测/version接口失败, 貌似云控未启动成功 ❌\n");
        } else {
            // 启动成功
            runLog.append("启动云控成功 ✔\n");
        }

        return runLog.toString();
    }

    public String startAgent(InstanceDO instance) {
        String ip = instance.getIp();
        Shell shell = new Shell(ip, "root", instance.getSshPort());
        String runLog = shell.execCommandPrvKey("cd /root/agents && ./dstagent start -d");
        return runLog;
    }

    @Override
    public void saveOperateLog(String name) {
        GameOperateLogDO record = new GameOperateLogDO();
        Long userId = HeaderHolder.getCurrentUserId();
        record.setUserId(userId);
//        record.setRentId();
        record.setName(name);
        operateLogMapper.insert(record);
    }

    @Override
    public void saveOperateLog(Long uid, String name) {
        GameOperateLogDO record = new GameOperateLogDO();
        record.setUserId(uid);
//        record.setRentId(0L);
        record.setName(name);
        operateLogMapper.insert(record);
    }

    @Override
    public void setMasterEqualPort(InstanceDO instance) {
        MasterServerIni masterServerIni = DstClient.getInstance().getMasterIni(instance);
        MasterServerIni.NETWORK masterNetwork = masterServerIni.getNETWORK();
        if (masterNetwork.getServer_port() != null && !masterNetwork.getServer_port().equals(instance.getPortDstMaster())) {
            masterNetwork.setServer_port(instance.getPortDstMaster());
            masterServerIni.setNETWORK(masterNetwork);
            DstClient.getInstance().updateMasterIni(instance, masterServerIni);
            log.info("instanceId:{} 地面已设置对等端口:{}", instance.getId(), instance.getPortDstMaster());
        }
    }

    @Override
    public void setCavesEqualPort(InstanceDO instance) {
        CavesServerIni cavesServerIni = DstClient.getInstance().getCavesIni(instance);
        CavesServerIni.NETWORK cavesNetwork = cavesServerIni.getNETWORK();
        if (cavesNetwork.getServer_port() != null && !cavesNetwork.getServer_port().equals(instance.getPortDstCaves())) {
            cavesNetwork.setServer_port(instance.getPortDstCaves());
            cavesServerIni.setNETWORK(cavesNetwork);
            DstClient.getInstance().updateCavesIni(instance, cavesServerIni);
            log.info("instanceId:{} 洞穴已设置对等端口:{}", instance.getId(), instance);
        }
    }
}
