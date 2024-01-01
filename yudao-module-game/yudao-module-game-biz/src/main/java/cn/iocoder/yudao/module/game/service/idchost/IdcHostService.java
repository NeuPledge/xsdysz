package cn.iocoder.yudao.module.game.service.idchost;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.game.controller.admin.idchost.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.idchost.IdcHostDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 机房主机 Service 接口
 *
 * @author 管理员
 */
public interface IdcHostService {

    /**
     * 创建机房主机
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createIdcHost(@Valid IdcHostSaveReqVO createReqVO);

    /**
     * 更新机房主机
     *
     * @param updateReqVO 更新信息
     */
    void updateIdcHost(@Valid IdcHostSaveReqVO updateReqVO);

    /**
     * 删除机房主机
     *
     * @param id 编号
     */
    void deleteIdcHost(Long id);

    /**
     * 获得机房主机
     *
     * @param id 编号
     * @return 机房主机
     */
    IdcHostDO getIdcHost(Long id);

    /**
     * 获得机房主机分页
     *
     * @param pageReqVO 分页查询
     * @return 机房主机分页
     */
    PageResult<IdcHostDO> getIdcHostPage(IdcHostPageReqVO pageReqVO);

}