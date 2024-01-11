package cn.iocoder.yudao.module.game.service.console.dst.timer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dooyo.dao.MBTDicDao;
import com.dooyo.dao.MBTInstanceDao;
import com.dooyo.dao.entity.MBTDicEntity;
import com.dooyo.dao.entity.MBTDicEntityExample;
import com.dooyo.dao.entity.MBTInstanceEntity;
import com.dooyo.service.IInstanceService;
import com.dooyo.service.clients.dst.DstClient;
import cn.iocoder.yudao.module.game.util.OKHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DstGameUpdateService {


    @Autowired
    IInstanceService instanceService;

    @Autowired
    MBTInstanceDao instanceDao;

    @Autowired
    MBTDicDao dicDao;

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
                    MBTDicEntityExample dicEntityExample = new MBTDicEntityExample();
                    MBTDicEntityExample.Criteria dicEntityExampleCriteria = dicEntityExample.createCriteria();
                    dicEntityExampleCriteria.andTypeEqualTo("dst_latest_version");
                    dicEntityExampleCriteria.andKeyEqualTo("dst_latest_version");
                    MBTDicEntity mbtDic = dicDao.selectOneByExample(dicEntityExample);
                    String latestVersion = mbtDic.getValue();

                    int dbVersion = Integer.parseInt(latestVersion);

                    if (version > dbVersion) {
                        log.info("检测到饥荒有更新, 当前版本{}, 最新版本{}, 需要更新", dbVersion, version);
                        mbtDic.setValue(String.valueOf(version));
                        dicDao.updateByPrimaryKeySelective(mbtDic, MBTDicEntity.Column.value);
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
        MBTDicEntityExample example = new MBTDicEntityExample();
        MBTDicEntityExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo("dst_host_check_version_instid");
        criteria.andKeyEqualTo("dst_host_check_version_instid");
        MBTDicEntity mbtDic = dicDao.selectOneByExample(example);
        String instanceId = mbtDic.getValue();
        MBTInstanceEntity instance = instanceDao.selectByPrimaryKey(Long.parseLong(instanceId));
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

            MBTDicEntityExample dicEntityExample = new MBTDicEntityExample();
            MBTDicEntityExample.Criteria dicEntityExampleCriteria = dicEntityExample.createCriteria();
            dicEntityExampleCriteria.andTypeEqualTo("dst_latest_version");
            dicEntityExampleCriteria.andKeyEqualTo("dst_latest_version");
            MBTDicEntity latestVersionEntity = dicDao.selectOneByExample(dicEntityExample);
            String preLatestVersion = latestVersionEntity.getValue();
            int preLatestVersionValue = Integer.parseInt(preLatestVersion);

            if (preLatestVersionValue > 0 && curLatestVersionValue > preLatestVersionValue) {
                log.info("检测到饥荒有更新, 当前版本{}, 最新版本{}, 需要更新", preLatestVersionValue, curLatestVersionValue);
                mbtDic.setValue(version);
                dicDao.updateByPrimaryKeySelective(mbtDic, MBTDicEntity.Column.value);

                MBTInstanceEntity record = new MBTInstanceEntity();
                record.setId(instance.getId());
                record.setDstGameVersion(version);
                instanceDao.updateByPrimaryKeySelective(record, MBTInstanceEntity.Column.dstGameVersion);
            }
        }
        // 更新全部的机器.
        log.info("开始更新全部的机器");
        run();
    }

}
