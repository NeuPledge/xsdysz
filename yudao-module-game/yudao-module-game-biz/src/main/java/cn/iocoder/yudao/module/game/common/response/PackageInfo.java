package cn.iocoder.yudao.module.game.common.response;

import com.dooyo.dao.entity.MBTDstPackageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;


@Data
public class PackageInfo extends MBTDstPackageEntity {
    @Schema(description = "套餐对应的价格")
    private Double price;

    static HashMap<Long, Double> priceMap = new HashMap<>();

    static {
        priceMap.put(1L, 15D);
        priceMap.put(2L, 25D);
        priceMap.put(3L, 30D);
        priceMap.put(4L, 38D);
        priceMap.put(5L, 48D);
        priceMap.put(6L, 58D);
        priceMap.put(7L, 80D);
        priceMap.put(8L, 66D);
        priceMap.put(9L, 118D);
        priceMap.put(10L, 148D);
    }

    public PackageInfo(MBTDstPackageEntity entity) {
        this.setId(entity.getId());
        this.setName(entity.getName());
        this.setPlayer(entity.getPlayer());
        this.setCpu(entity.getCpu());
        this.setMemoryMax(entity.getMemoryMax());
        this.setMemoryMin(entity.getMemoryMin());
        this.setBandwidthMax(entity.getBandwidthMax());
        this.setBandwidthMin(entity.getBandwidthMin());
        this.setCpuHzMin(entity.getCpuHzMin());
        this.setCpuHzMax(entity.getCpuHzMax());
        this.setCreateTime(entity.getCreateTime());
        this.setUpdateTime(entity.getUpdateTime());
        this.setPrice(priceMap.get(entity.getId()));
    }

    public PackageInfo() {
    }


}
