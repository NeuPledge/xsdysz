package cn.iocoder.yudao.module.debrief.service.diccollege;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.debrief.controller.admin.diccollege.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.diccollege.DicCollegeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.debrief.dal.mysql.diccollege.DicCollegeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.debrief.enums.ErrorCodeConstants.*;

/**
 * 学院字典列 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DicCollegeServiceImpl implements DicCollegeService {

    @Resource
    private DicCollegeMapper dicCollegeMapper;

    @Override
    public Long createDicCollege(DicCollegeSaveReqVO createReqVO) {
        // 插入
        DicCollegeDO dicCollege = BeanUtils.toBean(createReqVO, DicCollegeDO.class);
        dicCollegeMapper.insert(dicCollege);
        // 返回
        return dicCollege.getId();
    }

    @Override
    public void updateDicCollege(DicCollegeSaveReqVO updateReqVO) {
        // 校验存在
        validateDicCollegeExists(updateReqVO.getId());
        // 更新
        DicCollegeDO updateObj = BeanUtils.toBean(updateReqVO, DicCollegeDO.class);
        dicCollegeMapper.updateById(updateObj);
    }

    @Override
    public void deleteDicCollege(Long id) {
        // 校验存在
        validateDicCollegeExists(id);
        // 删除
        dicCollegeMapper.deleteById(id);
    }

    private void validateDicCollegeExists(Long id) {
        if (dicCollegeMapper.selectById(id) == null) {
            throw exception(DIC_COLLEGE_NOT_EXISTS);
        }
    }

    @Override
    public DicCollegeDO getDicCollege(Long id) {
        return dicCollegeMapper.selectById(id);
    }

    @Override
    public PageResult<DicCollegeDO> getDicCollegePage(DicCollegePageReqVO pageReqVO) {
        return dicCollegeMapper.selectPage(pageReqVO);
    }

}