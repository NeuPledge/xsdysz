package cn.iocoder.yudao.module.debrief.dal.mysql.dicbranch;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicbranch.DicBranchDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.debrief.controller.admin.dicbranch.vo.*;

/**
 * 支部字典列 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DicBranchMapper extends BaseMapperX<DicBranchDO> {

    default PageResult<DicBranchDO> selectPage(DicBranchPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DicBranchDO>()
                .likeIfPresent(DicBranchDO::getBranchName, reqVO.getBranchName())
                .betweenIfPresent(DicBranchDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DicBranchDO::getId));
    }

}