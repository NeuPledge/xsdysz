package cn.iocoder.yudao.module.game.service.console.dst.timer;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.module.game.dal.mysql.instance.InstanceMapper;
import cn.iocoder.yudao.module.game.service.console.clients.dst.ClientResponse;
import cn.iocoder.yudao.module.game.util.OKHttpUtil;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.dict.dto.DictDataRespDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class HealthCheckService {

    @Autowired
    InstanceMapper instanceDao;

    @Autowired
    DictDataApi dictDataApi;

    public void runSingle(Long instanceId) {
        InstanceDO mbtInstanceEntity = instanceDao.selectOne(new LambdaQueryWrapperX<InstanceDO>().eq(InstanceDO::getId, instanceId).ge(InstanceDO::getEndTime, new Date()));
        String url = "http://" + mbtInstanceEntity.getIp() + ":" + mbtInstanceEntity.getAgentPort() + "/dst-agent/api/version";
        try {
            String response = OKHttpUtil.sendGet(url);
            ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
            });
            if (clientResponse.getCode() == 200) {
                mbtInstanceEntity.setAgentHeartbeatTime(LocalDateTime.now());
                mbtInstanceEntity.setAgentVersion(clientResponse.getData());
                mbtInstanceEntity.setAgentStatus("up");
                instanceDao.updateById(mbtInstanceEntity);
            }
        } catch (Exception ignore) {
            log.error("出错url:{}", url);
            mbtInstanceEntity.setAgentStatus("down");
            instanceDao.updateById(mbtInstanceEntity);
        }
    }

    public void run() {

        DictDataRespDTO mbtDic = dictDataApi.parseDictData("dst_agent_latest_version", "dst_agent_latest_version");
        String latestVersion = mbtDic.getValue();

        List<InstanceDO> mbtInstanceEntities = instanceDao.selectList(new LambdaQueryWrapperX<InstanceDO>()
                .ne(InstanceDO::getAgentVersion, latestVersion)
                .ne(InstanceDO::getSale, 1)
                .ge(InstanceDO::getEndTime, new Date()));

        CountDownLatch countDownLatch = new CountDownLatch(mbtInstanceEntities.size());
        ExecutorService executor = new ThreadPoolExecutor(30, 30, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        for (InstanceDO mbtInstanceEntity : mbtInstanceEntities) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    // 服务器到期之后, 不做心跳检查.
                    if (mbtInstanceEntity.getEndTime().isBefore(LocalDateTime.now())) {
                        mbtInstanceEntity.setAgentStatus("down");
                        instanceDao.updateById(mbtInstanceEntity);
                        countDownLatch.countDown();
                        return;
                    }
                    // 服务器心跳超过5天, 不再发检查.
                    if (mbtInstanceEntity.getAgentHeartbeatTime().plusDays(5).isBefore(LocalDateTime.now())) {
                        countDownLatch.countDown();
                        return;
                    }

                    String url = "http://" + mbtInstanceEntity.getIp() + ":" + mbtInstanceEntity.getAgentPort() + "/dst-agent/api/version";
                    try {
                        String response = OKHttpUtil.sendGet(url);
                        log.info("机器ID:{}, IP:{}, 获取version成功:{}.", mbtInstanceEntity.getId(), mbtInstanceEntity.getIp(), response);
                        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
                        });
                        if (clientResponse.getCode() == 200 && mbtInstanceEntity.getAgentVersion() != null && clientResponse.getData() != null && mbtInstanceEntity.getAgentVersion().equals(clientResponse.getData())) {
                            mbtInstanceEntity.setAgentHeartbeatTime(LocalDateTime.now());
                            mbtInstanceEntity.setAgentVersion(clientResponse.getData());
                            mbtInstanceEntity.setAgentStatus("up");
                            instanceDao.updateById(mbtInstanceEntity);
                        }
                    } catch (Exception ignore) {
                        log.error("出错url:{}", url);
                        mbtInstanceEntity.setAgentStatus("down");
                        instanceDao.updateById(mbtInstanceEntity);
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });

        }
        try {
            executor.shutdown();
            countDownLatch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
