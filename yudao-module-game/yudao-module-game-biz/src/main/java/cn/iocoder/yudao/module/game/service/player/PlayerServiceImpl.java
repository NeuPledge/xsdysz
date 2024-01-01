package cn.iocoder.yudao.module.game.service.player;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.game.controller.admin.player.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.player.PlayerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.game.dal.mysql.player.PlayerMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.game.enums.ErrorCodeConstants.*;

/**
 * 记录局内玩家人数 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class PlayerServiceImpl implements PlayerService {

    @Resource
    private PlayerMapper playerMapper;

    @Override
    public Long createPlayer(PlayerSaveReqVO createReqVO) {
        // 插入
        PlayerDO player = BeanUtils.toBean(createReqVO, PlayerDO.class);
        playerMapper.insert(player);
        // 返回
        return player.getId();
    }

    @Override
    public void updatePlayer(PlayerSaveReqVO updateReqVO) {
        // 校验存在
        validatePlayerExists(updateReqVO.getId());
        // 更新
        PlayerDO updateObj = BeanUtils.toBean(updateReqVO, PlayerDO.class);
        playerMapper.updateById(updateObj);
    }

    @Override
    public void deletePlayer(Long id) {
        // 校验存在
        validatePlayerExists(id);
        // 删除
        playerMapper.deleteById(id);
    }

    private void validatePlayerExists(Long id) {
        if (playerMapper.selectById(id) == null) {
            throw exception(PLAYER_NOT_EXISTS);
        }
    }

    @Override
    public PlayerDO getPlayer(Long id) {
        return playerMapper.selectById(id);
    }

    @Override
    public PageResult<PlayerDO> getPlayerPage(PlayerPageReqVO pageReqVO) {
        return playerMapper.selectPage(pageReqVO);
    }

}