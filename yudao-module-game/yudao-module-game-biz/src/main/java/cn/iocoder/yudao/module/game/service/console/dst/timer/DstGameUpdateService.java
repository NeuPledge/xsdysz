package cn.iocoder.yudao.module.game.service.console.dst.timer;

import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.module.game.dal.mysql.instance.InstanceMapper;
import cn.iocoder.yudao.module.game.service.console.clients.dst.DstClient;
import cn.iocoder.yudao.module.game.service.console.dst.IInstanceService;
import cn.iocoder.yudao.module.game.util.OKHttpUtil;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.dict.dto.DictDataRespDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class DstGameUpdateService {


    @Autowired
    IInstanceService instanceService;

    @Autowired
    InstanceMapper instanceDao;

    @Resource
    DictDataApi dictDataApi;

//    @Autowired
//    MBTDicDao dicDao;

    public void run() {
        // 更新
        instanceService.updateAllDstGame();
        // 查询版本号并入库
//        instanceService.getAllInstancesDstGameVersion();
    }

    // 从第三方的网站检测最新版本, 但是这个网站偶尔会挂
    public void checkLatestVersion() {
        String response = null;
        try {
            response = OKHttpUtil.sendGet("https://api.dstserverlist.top/api/server/version");
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("从第三方网站查询饥荒最新版本出错.");
            return;
        }
        if (StringUtils.isNotBlank(response)) {
            JSONObject jsonObject = JSON.parseObject(response);
            if (jsonObject != null) {
                Integer version = jsonObject.getInteger("version");
                if (version != null && version > 0) {
                    DictDataRespDTO dictDataRespDTO = dictDataApi.parseDictData("dst_latest_version", "dst_latest_version");
                    String latestVersion = dictDataRespDTO.getValue();
                    int dbVersion = Integer.parseInt(latestVersion);

                    if (version > dbVersion) {
                        log.info("检测到饥荒有更新, 当前版本{}, 最新版本{}, 需要更新", dbVersion, version);
//                        mbtDic.setValue(String.valueOf(version));
//                        dicDao.updateByPrimaryKeySelective(mbtDic, MBTDicEntity.Column.value);
                        run();
                    } else {
                        log.info("从第三方网站查询饥荒最新版本, 当前版本{}, 最新版本{}, 无需更新", dbVersion, version);
                    }
                }
            }
        }
        System.out.println(response);
    }


    /**
     * 定时更新我们自己的那台服务器的饥荒版本. 如果有更新, 则触发全局更新.
     */
    public void updateSelfDstVersion() {

        DictDataRespDTO dictDataRespDTO = dictDataApi.parseDictData("dst_host_check_version_instid", "dst_host_check_version_instid");
        String instanceId = dictDataRespDTO.getValue();

        InstanceDO instance = instanceDao.selectById(Long.parseLong(instanceId));
        DstClient.getInstance().updateGame(instance);


        log.info("机器ID:{}, IP:{}, 更新游戏完成.", instance.getId(), instance.getIp());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String version = instanceService.getDstGameVersion(instance);

        if (StringUtils.isNotEmpty(version) && StringUtils.isNotEmpty(version.trim())) {
            int curLatestVersionValue = Integer.parseInt(version);

            DictDataRespDTO lastVersionDto = dictDataApi.parseDictData("dst_latest_version", "dst_latest_version");
            String preLatestVersion = lastVersionDto.getValue();

            int preLatestVersionValue = Integer.parseInt(preLatestVersion);

            if (preLatestVersionValue > 0 && curLatestVersionValue > preLatestVersionValue) {
                log.info("检测到饥荒有更新, 当前版本{}, 最新版本{}, 需要更新", preLatestVersionValue, curLatestVersionValue);
//                mbtDic.setValue(version);
//                dicDao.updateByPrimaryKeySelective(mbtDic, MBTDicEntity.Column.value);

//                MBTInstanceEntity record = new MBTInstanceEntity();
                instance.setId(instance.getId());
                instance.setGameVersionDst(version);
                instanceDao.updateById(instance);
            }
        }
        // 更新全部的机器.
        log.info("开始更新全部的机器");
        run();
    }

}
