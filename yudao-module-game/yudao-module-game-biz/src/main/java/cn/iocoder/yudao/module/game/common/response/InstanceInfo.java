package cn.iocoder.yudao.module.game.common.response;

import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 机器管理列表
 */
@Getter
@Setter
public class InstanceInfo extends InstanceDO {

    @Schema(description = "是否已售")
    private Boolean sale;
    private Long userId;
    private Long rentId;
    @Schema(description = "适用的套餐id")
    private List<Long> suitablePackages;
    @Schema(description = "绑定租约起始时间")
    private LocalDateTime rentStartTime;
    @Schema(description = "绑定租约到期时间")
    private LocalDateTime rentEndTime;
    
    @Schema(description = "机器到期剩余天数")
    private String instanceLeftDays;
    @Schema(description = "二手转出价格")
    private String instanceSafePrice;

    public InstanceInfo() {
    }

    public InstanceInfo(InstanceDO entity) {
        this.setAgentHeartbeatTime(entity.getAgentHeartbeatTime());
        this.setAgentStatus(entity.getAgentStatus());
        this.setAgentStartTime(entity.getAgentStartTime());

        this.setCompanyCode(entity.getCompanyCode());
        this.setCpuHz(entity.getCpuHz());
        this.setCpu(entity.getCpu());
        this.setCreateTime(entity.getCreateTime());
        this.setAgentHeartbeatTime(entity.getAgentHeartbeatTime());

        this.setAgentStartTime(entity.getAgentStartTime());
        this.setAgentStatus(entity.getAgentStatus());
        this.setAgentVersion(entity.getAgentVersion());
        this.setEndTime(entity.getEndTime());
        this.setId(entity.getId());

        this.setIsOwn(entity.getIsOwn());
        this.setIp(entity.getIp());
        this.setMemory(entity.getMemory());
        this.setRentEndTime(entity.getRentEndTime());

        this.setResourceAccount(entity.getResourceAccount());
        this.setResourceAccountPassword(entity.getResourceAccountPassword());
        this.setRegion(entity.getRegion());
        this.setStartTime(entity.getStartTime());

        this.setWayOfBuy(entity.getWayOfBuy());
        this.setPackageName(entity.getPackageName());
        this.setPrice(entity.getPrice());
        this.setUpdateTime(entity.getUpdateTime());
    }
}
