package cn.iocoder.yudao.module.debrief.service.cache;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.debrief.controller.app.response.DicCollegeResponse;
import cn.iocoder.yudao.module.debrief.controller.app.response.DicGradeResponse;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicbranch.DicBranchDO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.diccollege.DicCollegeDO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicgrade.DicGradeDO;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicbranch.DicBranchMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.diccollege.DicCollegeMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicgrade.DicGradeMapper;
import cn.iocoder.yudao.module.debrief.service.DicService;
import cn.iocoder.yudao.module.debrief.util.DebriefConverter;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class DicCacheServiceImpl implements DicService {

    @Resource
    DicCollegeMapper dicCollegeMapper;
    @Resource
    DicGradeMapper dicGradeMapper;
    @Resource
    DicBranchMapper dicBranchMapper;

    private static final String COLLEGE_KEY = "college";

    private static final String BRANCH_KEY = "branch";

    private final Cache<String, List<DicCollegeResponse>> collegesCache = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .initialCapacity(1)
            .maximumSize(1)
            .build();
    private final Cache<Long, List<DicGradeResponse>> gradesCache = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .initialCapacity(18)
            .maximumSize(20)
            .build();
    private final Cache<Long, String> gradeCache = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .initialCapacity(20)
            .maximumSize(100)
            .build();
    private final Cache<Long, String> branchCache = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .initialCapacity(20)
            .maximumSize(100)
            .build();

    // private void testCache(){
    //     gradeCache.stats();
    // }

    @Override
    public String getBranchName(Long branchId) {
        String response;
        String branchName = branchCache.getIfPresent(branchId);
        if (StringUtils.isEmpty(branchName)) {
            DicBranchDO branch = dicBranchMapper.selectById(branchId);
            if (Objects.isNull(branch)) {
                throw new ServiceException("未找到对应支部");
            }
            response = branch.getBranchName();
            branchCache.put(branchId, branch.getBranchName());
        } else {
            response = branchName;
        }
        return response;
    }

    @Override
    public List<DicCollegeResponse> getColleges() {
        List<DicCollegeResponse> responses;
        List<DicCollegeResponse> colleges = collegesCache.get(COLLEGE_KEY, key -> new ArrayList<>());
        if (CollectionUtils.isEmpty(colleges)) {
            List<DicCollegeDO> dicColleges = dicCollegeMapper.selectList();
            List<DicCollegeResponse> dicCollegeResponses = DebriefConverter
                    .convertToDtoList(dicColleges, DicCollegeResponse.class);
            collegesCache.put(COLLEGE_KEY, dicCollegeResponses);
            responses = dicCollegeResponses;
        } else {
            responses = colleges;
        }
        return responses;
    }

    @Override
    public List<DicGradeResponse> getGrades(Long collegeId) {
        List<DicGradeResponse> responses;
        List<DicGradeResponse> grades = gradesCache.get(collegeId, key -> new ArrayList<>());
        if (CollectionUtils.isEmpty(grades)) {
            List<DicGradeDO> dicGrades = dicGradeMapper.selectList(
                    new LambdaQueryWrapperX<DicGradeDO>().eq(DicGradeDO::getCollegeId, collegeId));
            List<DicGradeResponse> dicGradeResponses = DebriefConverter
                    .convertToDtoList(dicGrades, DicGradeResponse.class);
            gradesCache.put(collegeId, dicGradeResponses);
            responses = dicGradeResponses;
        } else {
            responses = grades;
        }
        return responses;
    }

    @Override
    public String getGrade(Long id) {
        String response;
        String gradeName = gradeCache.getIfPresent(id);
        if (StringUtils.isEmpty(gradeName)) {
            DicGradeDO grade = dicGradeMapper.selectById(id);
            if (Objects.isNull(grade)) {
                throw new ServiceException("未找到对应班级");
            }
            gradeCache.put(id, grade.getGradeName());
            response = grade.getGradeName();
        } else {
            response = gradeName;
        }
        return response;
    }
}
