package cn.iocoder.yudao.module.game.service.idchost;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.game.controller.admin.idchost.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.idchost.IdcHostDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.game.dal.mysql.idchost.IdcHostMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.game.enums.ErrorCodeConstants.*;

/**
 * 机房主机 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class IdcHostServiceImpl implements IdcHostService {

    @Resource
    private IdcHostMapper idcHostMapper;

    @Override
    public Long createIdcHost(IdcHostSaveReqVO createReqVO) {
        // 插入
        IdcHostDO idcHost = BeanUtils.toBean(createReqVO, IdcHostDO.class);
        idcHostMapper.insert(idcHost);
        // 返回
        return idcHost.getId();
    }

    @Override
    public void updateIdcHost(IdcHostSaveReqVO updateReqVO) {
        // 校验存在
        validateIdcHostExists(updateReqVO.getId());
        // 更新
        IdcHostDO updateObj = BeanUtils.toBean(updateReqVO, IdcHostDO.class);
        idcHostMapper.updateById(updateObj);
    }

    @Override
    public void deleteIdcHost(Long id) {
        // 校验存在
        validateIdcHostExists(id);
        // 删除
        idcHostMapper.deleteById(id);
    }

    private void validateIdcHostExists(Long id) {
        if (idcHostMapper.selectById(id) == null) {
            throw exception(IDC_HOST_NOT_EXISTS);
        }
    }

    @Override
    public IdcHostDO getIdcHost(Long id) {
        return idcHostMapper.selectById(id);
    }

    @Override
    public PageResult<IdcHostDO> getIdcHostPage(IdcHostPageReqVO pageReqVO) {
        return idcHostMapper.selectPage(pageReqVO);
    }

}