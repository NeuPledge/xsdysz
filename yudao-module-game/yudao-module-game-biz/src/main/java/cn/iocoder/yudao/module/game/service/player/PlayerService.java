package cn.iocoder.yudao.module.game.service.player;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.game.controller.admin.player.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.player.PlayerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 记录局内玩家人数 Service 接口
 *
 * @author 管理员
 */
public interface PlayerService {

    /**
     * 创建记录局内玩家人数
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPlayer(@Valid PlayerSaveReqVO createReqVO);

    /**
     * 更新记录局内玩家人数
     *
     * @param updateReqVO 更新信息
     */
    void updatePlayer(@Valid PlayerSaveReqVO updateReqVO);

    /**
     * 删除记录局内玩家人数
     *
     * @param id 编号
     */
    void deletePlayer(Long id);

    /**
     * 获得记录局内玩家人数
     *
     * @param id 编号
     * @return 记录局内玩家人数
     */
    PlayerDO getPlayer(Long id);

    /**
     * 获得记录局内玩家人数分页
     *
     * @param pageReqVO 分页查询
     * @return 记录局内玩家人数分页
     */
    PageResult<PlayerDO> getPlayerPage(PlayerPageReqVO pageReqVO);

}