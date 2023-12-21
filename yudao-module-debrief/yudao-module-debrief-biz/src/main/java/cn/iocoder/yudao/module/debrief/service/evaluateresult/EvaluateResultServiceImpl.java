package cn.iocoder.yudao.module.debrief.service.evaluateresult;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.debrief.controller.admin.diccollege.vo.StypeCount;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.BranchProgressDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.CollegeProgressDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.CommentTrendDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.CtypeResult;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.GradeProgressDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.ProgressTrendDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.UnCommentStudent;
import cn.iocoder.yudao.module.debrief.dal.dataobject.diccollege.DicCollegeDO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.partymember.PartyMemberDO;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicbranch.DicBranchMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.diccollege.DicCollegeMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicgrade.DicGradeMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.partymember.PartyMemberMapper;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.dict.dto.DictDataRespDTO;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.evaluateresult.EvaluateResultDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.debrief.dal.mysql.evaluateresult.EvaluateResultMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.debrief.enums.ErrorCodeConstants.*;

/**
 * 评价结果 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class EvaluateResultServiceImpl implements EvaluateResultService {

    @Resource
    private EvaluateResultMapper evaluateResultMapper;

    @Resource
    private DicBranchMapper dicBranchMapper;

    @Resource
    private DicGradeMapper dicGradeMapper;

    @Resource
    private DictDataApi dictDataApi;

    @Resource
    private DicCollegeMapper dicCollegeMapper;

    @Resource
    private PartyMemberMapper partyMemberMapper;

    @Override
    public Long createEvaluateResult(EvaluateResultSaveReqVO createReqVO) {
        // 插入
        EvaluateResultDO evaluateResult = BeanUtils.toBean(createReqVO, EvaluateResultDO.class);
        evaluateResultMapper.insert(evaluateResult);
        // 返回
        return evaluateResult.getId();
    }

    @Override
    public void updateEvaluateResult(EvaluateResultSaveReqVO updateReqVO) {
        // 校验存在
        validateEvaluateResultExists(updateReqVO.getId());
        // 更新
        EvaluateResultDO updateObj = BeanUtils.toBean(updateReqVO, EvaluateResultDO.class);
        evaluateResultMapper.updateById(updateObj);
    }

    @Override
    public void deleteEvaluateResult(Long id) {
        // 校验存在
        validateEvaluateResultExists(id);
        // 删除
        evaluateResultMapper.deleteById(id);
    }

    private void validateEvaluateResultExists(Long id) {
        if (evaluateResultMapper.selectById(id) == null) {
            throw exception(EVALUATE_RESULT_NOT_EXISTS);
        }
    }

    @Override
    public EvaluateResultDO getEvaluateResult(Long id) {
        return evaluateResultMapper.selectById(id);
    }

    @Override
    public PageResult<EvaluateResultDO> getEvaluateResultPage(EvaluateResultPageReqVO pageReqVO) {
        return evaluateResultMapper.selectPage(pageReqVO);
    }


    @Override
    public ProgressTrendRespVO finishProgressTrend() {

        DictDataRespDTO dictData = dictDataApi.parseDictData("debrief_dict", "测评起始时间");
        String startDate = dictData.getValue();
        List<ProgressTrendDto> progressTrendDtos = evaluateResultMapper.finishProgressTrend(startDate);

        ProgressTrendRespVO progressTrendRespVO = new ProgressTrendRespVO();
        List<String> days = new ArrayList<>();
        List<Long> dayCounts = new ArrayList<>();
        List<Long> totalCounts = new ArrayList<>();
        for (ProgressTrendDto progressTrendDto : progressTrendDtos) {
            days.add(progressTrendDto.getDay());
            dayCounts.add(progressTrendDto.getDayCount());
            totalCounts.add(progressTrendDto.getTotalCount());
        }
        progressTrendRespVO.setDays(days);
        progressTrendRespVO.setDayCounts(dayCounts);
        progressTrendRespVO.setTotalCounts(totalCounts);

        return progressTrendRespVO;
    }

    @Override
    public CommentTrendRespVO commentTrend() {
        DictDataRespDTO dictData = dictDataApi.parseDictData("debrief_dict", "测评起始时间");
        String startDate = dictData.getValue();
        List<CommentTrendDto> commentTrendDtos = evaluateResultMapper.commentTrend(startDate);
        Map<String, List<CommentTrendDto>> dayMap = commentTrendDtos.stream().collect(Collectors.groupingBy(e -> e.getDay(), Collectors.toList()));

        dayMap = sortMapByDate(dayMap);

        List<String> days = new ArrayList<>();
        List<Long> comment1List = new ArrayList<>();
        List<Long> comment2List = new ArrayList<>();
        List<Long> comment3List = new ArrayList<>();
        for (String day : dayMap.keySet()) {
            List<CommentTrendDto> comments = dayMap.get(day);
            long comment1 = 0;
            long comment2 = 0;
            long comment3 = 0;
            for (CommentTrendDto comment : comments) {
                if (comment.getComment() == 1) {
                    comment1 = comment.getNum();
                } else if (comment.getComment() == 2) {
                    comment2 = comment.getNum();
                } else if (comment.getComment() == 3) {
                    comment3 = comment.getNum();
                }
            }
            days.add(day);
            comment1List.add(comment1);
            comment2List.add(comment2);
            comment3List.add(comment3);
        }
        CommentTrendRespVO commentTrendRespVO = new CommentTrendRespVO();
        commentTrendRespVO.setDays(days);
        commentTrendRespVO.setComment1(comment1List);
        commentTrendRespVO.setComment2(comment2List);
        commentTrendRespVO.setComment3(comment3List);
        return commentTrendRespVO;
    }


    public static Map<String, List<CommentTrendDto>> sortMapByDate(Map<String, List<CommentTrendDto>> inputMap) {
        // 使用LinkedHashMap保持插入顺序
        LinkedHashMap<String, List<CommentTrendDto>> sortedMap = inputMap.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return sortedMap;
    }

    @Override
    public List<CollegeProgressDto> collegeProgress() {
        List<CollegeProgressDto> collegeProgressDtos = evaluateResultMapper.collegeProgress();
        return collegeProgressDtos;
    }

    @Override
    public BranchProgressRespVo branchProgress() {

        BranchProgressRespVo branchProgressRespVo = new BranchProgressRespVo();

        List<BranchProgressDto> branchProgress = evaluateResultMapper.branchProgress();
        List<BranchProgressDto> list = new ArrayList<>();
        int i = 1;

        Long branchCount = dicBranchMapper.selectCount();
        int finishCount = 0;
        for (BranchProgressDto progress : branchProgress) {
            if (progress.getNum() >= progress.getTotal()) {
                finishCount++;
            }
            if (i <= 20) {
                list.add(progress);
            }
            i++;

        }
        branchProgressRespVo.setList(list);
        branchProgressRespVo.setProgress(finishCount + "/" + branchCount);

        return branchProgressRespVo;
    }

    @Override
    public GradeProgressRespVo gradeProgress() {

        GradeProgressRespVo gradeProgressRespVo = new GradeProgressRespVo();

        List<GradeProgressDto> progressDtos = evaluateResultMapper.gradeProgress();

        List<GradeProgressDto> list = new ArrayList<>();
        int i = 1;

        Long branchCount = dicGradeMapper.selectCount();
        int finishCount = 0;
        for (GradeProgressDto progress : progressDtos) {
            if (progress.getNum() >= progress.getTotal()) {
                finishCount++;
            }
            if (i <= 40) {
                list.add(progress);
            }
            i++;

        }
        gradeProgressRespVo.setList(list);

        gradeProgressRespVo.setProgress(finishCount + "/" + branchCount);

        return gradeProgressRespVo;
    }

    @Override
    public List<UnCommentStudent> uncommentStudent(Long collegeId) {
        List<UnCommentStudent> unCommentStudents = evaluateResultMapper.unCommentStudent(collegeId);
        return unCommentStudents;
    }

    @Override
    public void analysisReport(Long collegeId, HttpServletResponse response) throws IOException {

        DicCollegeDO dicCollegeDO = dicCollegeMapper.selectById(collegeId);
        DictDataRespDTO dictData = dictDataApi.parseDictData("debrief_dict", "测评年度");

        AnalysisTemplate analysisTemplate = new AnalysisTemplate();

        analysisTemplate.setYear(dictData.getValue());
        analysisTemplate.setCollegeName(dicCollegeDO.getCollegeName());
        analysisTemplate.setDateymd(DateUtil.formatChineseDate(new Date(), false, false));


        List<StypeCount> stypeCounts = dicCollegeMapper.stypeCount(collegeId);
        List<StypeCount> yanjiusheng = stypeCounts.stream().filter(e -> e.getStype().equals("yanjiusheng")).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(yanjiusheng)) {
            analysisTemplate.setYanJiuShengPartyMemberCount(0);
        } else {
            Integer num = yanjiusheng.get(0).getNum();
            analysisTemplate.setYanJiuShengPartyMemberCount(num);
        }
        List<StypeCount> benke = stypeCounts.stream().filter(e -> e.getStype().equals("benke")).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(benke)) {
            analysisTemplate.setBenKePartyMemberCount(0);
        } else {
            Integer num = benke.get(0).getNum();
            analysisTemplate.setBenKePartyMemberCount(num);
        }
        analysisTemplate.setCoverPartyMemberCount(analysisTemplate.getYanJiuShengPartyMemberCount() + analysisTemplate.getBenKePartyMemberCount());

        Integer studentCount = dicCollegeMapper.studentCount(collegeId);
        analysisTemplate.setCoverStudentCount(studentCount);

        Long partyMemberCount = partyMemberMapper.selectCount(new LambdaQueryWrapperX<PartyMemberDO>().eq(PartyMemberDO::getCollegeId, collegeId));
        analysisTemplate.setAllPartyMemberCount(partyMemberCount.intValue());


        List<CtypeResult> c1Type = evaluateResultMapper.c1Result(collegeId, "c1_type");
        Map<String, CtypeResult> c1Map = c1Type.stream().collect(Collectors.toMap(e -> e.getCtype() + "_" + e.getStype(), e -> e));
        CtypeResult defaultV = new CtypeResult();
        defaultV.setNum(0);
        defaultV.setRate(0.0);
        analysisTemplate.setC1_1_1(c1Map.getOrDefault("c1_100_benke", defaultV).getNum());
        analysisTemplate.setC1_2_1(c1Map.getOrDefault("c1_90_benke", defaultV).getNum());
        analysisTemplate.setC1_3_1(c1Map.getOrDefault("c1_80_benke", defaultV).getNum());
        analysisTemplate.setC1_4_1(c1Map.getOrDefault("c1_70_benke", defaultV).getNum());
        analysisTemplate.setC1_5_1(c1Map.getOrDefault("c1_60_benke", defaultV).getNum());
        analysisTemplate.setC1_6_1(c1Map.getOrDefault("c1_50_benke", defaultV).getNum());
        analysisTemplate.setC1_7_1(c1Map.getOrDefault("c1_less_50_benke", defaultV).getNum());
        analysisTemplate.setC1_1_2(c1Map.getOrDefault("c1_100_benke", defaultV).getRate());
        analysisTemplate.setC1_2_2(c1Map.getOrDefault("c1_90_benke", defaultV).getRate());
        analysisTemplate.setC1_3_2(c1Map.getOrDefault("c1_80_benke", defaultV).getRate());
        analysisTemplate.setC1_4_2(c1Map.getOrDefault("c1_70_benke", defaultV).getRate());
        analysisTemplate.setC1_5_2(c1Map.getOrDefault("c1_60_benke", defaultV).getRate());
        analysisTemplate.setC1_6_2(c1Map.getOrDefault("c1_50_benke", defaultV).getRate());
        analysisTemplate.setC1_7_2(c1Map.getOrDefault("c1_less_50_benke", defaultV).getRate());

        analysisTemplate.setC1_1_3(c1Map.getOrDefault("c1_100_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC1_2_3(c1Map.getOrDefault("c1_90_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC1_3_3(c1Map.getOrDefault("c1_80_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC1_4_3(c1Map.getOrDefault("c1_70_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC1_5_3(c1Map.getOrDefault("c1_60_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC1_6_3(c1Map.getOrDefault("c1_50_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC1_7_3(c1Map.getOrDefault("c1_less_50_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC1_1_4(c1Map.getOrDefault("c1_100_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC1_2_4(c1Map.getOrDefault("c1_90_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC1_3_4(c1Map.getOrDefault("c1_80_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC1_4_4(c1Map.getOrDefault("c1_70_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC1_5_4(c1Map.getOrDefault("c1_60_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC1_6_4(c1Map.getOrDefault("c1_50_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC1_7_4(c1Map.getOrDefault("c1_less_50_yanjiusheng", defaultV).getRate());

        analysisTemplate.setC1_1_5(analysisTemplate.getC1_1_3() + analysisTemplate.getC1_1_1());
        analysisTemplate.setC1_2_5(analysisTemplate.getC1_2_3() + analysisTemplate.getC1_2_1());
        analysisTemplate.setC1_3_5(analysisTemplate.getC1_3_3() + analysisTemplate.getC1_3_1());
        analysisTemplate.setC1_4_5(analysisTemplate.getC1_4_3() + analysisTemplate.getC1_4_1());
        analysisTemplate.setC1_5_5(analysisTemplate.getC1_5_3() + analysisTemplate.getC1_5_1());
        analysisTemplate.setC1_6_5(analysisTemplate.getC1_6_3() + analysisTemplate.getC1_6_1());
        analysisTemplate.setC1_7_5(analysisTemplate.getC1_7_3() + analysisTemplate.getC1_7_1());
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        analysisTemplate.setC1_1_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC1_1_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC1_2_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC1_2_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC1_3_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC1_3_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC1_4_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC1_4_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC1_5_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC1_5_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC1_6_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC1_6_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC1_7_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC1_7_5() / analysisTemplate.getCoverPartyMemberCount())));


        List<CtypeResult> c12Type = evaluateResultMapper.c1Result(collegeId, "c12_type");
        Map<String, CtypeResult> c12Map = c12Type.stream().collect(Collectors.toMap(e -> e.getCtype() + "_" + e.getStype(), e -> e));
        analysisTemplate.setC12_1_1(c12Map.getOrDefault("c12_100_benke", defaultV).getNum());
        analysisTemplate.setC12_2_1(c12Map.getOrDefault("c12_90_benke", defaultV).getNum());
        analysisTemplate.setC12_3_1(c12Map.getOrDefault("c12_80_benke", defaultV).getNum());
        analysisTemplate.setC12_4_1(c12Map.getOrDefault("c12_70_benke", defaultV).getNum());
        analysisTemplate.setC12_5_1(c12Map.getOrDefault("c12_60_benke", defaultV).getNum());
        analysisTemplate.setC12_6_1(c12Map.getOrDefault("c12_50_benke", defaultV).getNum());
        analysisTemplate.setC12_7_1(c12Map.getOrDefault("c12_less_50_benke", defaultV).getNum());
        analysisTemplate.setC12_1_2(c12Map.getOrDefault("c12_100_benke", defaultV).getRate());
        analysisTemplate.setC12_2_2(c12Map.getOrDefault("c12_90_benke", defaultV).getRate());
        analysisTemplate.setC12_3_2(c12Map.getOrDefault("c12_80_benke", defaultV).getRate());
        analysisTemplate.setC12_4_2(c12Map.getOrDefault("c12_70_benke", defaultV).getRate());
        analysisTemplate.setC12_5_2(c12Map.getOrDefault("c12_60_benke", defaultV).getRate());
        analysisTemplate.setC12_6_2(c12Map.getOrDefault("c12_50_benke", defaultV).getRate());
        analysisTemplate.setC12_7_2(c12Map.getOrDefault("c12_less_50_benke", defaultV).getRate());

        analysisTemplate.setC12_1_3(c12Map.getOrDefault("c12_100_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC12_2_3(c12Map.getOrDefault("c12_90_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC12_3_3(c12Map.getOrDefault("c12_80_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC12_4_3(c12Map.getOrDefault("c12_70_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC12_5_3(c12Map.getOrDefault("c12_60_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC12_6_3(c12Map.getOrDefault("c12_50_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC12_7_3(c12Map.getOrDefault("c12_less_50_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC12_1_4(c12Map.getOrDefault("c12_100_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC12_2_4(c12Map.getOrDefault("c12_90_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC12_3_4(c12Map.getOrDefault("c12_80_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC12_4_4(c12Map.getOrDefault("c12_70_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC12_5_4(c12Map.getOrDefault("c12_60_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC12_6_4(c12Map.getOrDefault("c12_50_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC12_7_4(c12Map.getOrDefault("c12_less_50_yanjiusheng", defaultV).getRate());

        analysisTemplate.setC12_1_5(analysisTemplate.getC12_1_3() + analysisTemplate.getC12_1_1());
        analysisTemplate.setC12_2_5(analysisTemplate.getC12_2_3() + analysisTemplate.getC12_2_1());
        analysisTemplate.setC12_3_5(analysisTemplate.getC12_3_3() + analysisTemplate.getC12_3_1());
        analysisTemplate.setC12_4_5(analysisTemplate.getC12_4_3() + analysisTemplate.getC12_4_1());
        analysisTemplate.setC12_5_5(analysisTemplate.getC12_5_3() + analysisTemplate.getC12_5_1());
        analysisTemplate.setC12_6_5(analysisTemplate.getC12_6_3() + analysisTemplate.getC12_6_1());
        analysisTemplate.setC12_7_5(analysisTemplate.getC12_7_3() + analysisTemplate.getC12_7_1());
        analysisTemplate.setC12_1_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC12_1_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC12_2_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC12_2_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC12_3_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC12_3_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC12_4_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC12_4_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC12_5_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC12_5_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC12_6_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC12_6_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC12_7_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC12_7_5() / analysisTemplate.getCoverPartyMemberCount())));

        List<CtypeResult> c3Type = evaluateResultMapper.c1Result(collegeId, "c3_type");
        Map<String, CtypeResult> c3Map = c3Type.stream().collect(Collectors.toMap(e -> e.getCtype() + "_" + e.getStype(), e -> e));
        analysisTemplate.setC3_1_1(c3Map.getOrDefault("c3_less_10_benke", defaultV).getNum());
        analysisTemplate.setC3_2_1(c3Map.getOrDefault("c3_10_benke", defaultV).getNum());
        analysisTemplate.setC3_3_1(c3Map.getOrDefault("c3_20_benke", defaultV).getNum());
        analysisTemplate.setC3_4_1(c3Map.getOrDefault("c3_30_benke", defaultV).getNum());
        analysisTemplate.setC3_5_1(c3Map.getOrDefault("c3_40_benke", defaultV).getNum());
        analysisTemplate.setC3_6_1(c3Map.getOrDefault("c3_less_50_benke", defaultV).getNum());
        analysisTemplate.setC3_1_2(c3Map.getOrDefault("c3_less_10_benke", defaultV).getRate());
        analysisTemplate.setC3_2_2(c3Map.getOrDefault("c3_10_benke", defaultV).getRate());
        analysisTemplate.setC3_3_2(c3Map.getOrDefault("c3_20_benke", defaultV).getRate());
        analysisTemplate.setC3_4_2(c3Map.getOrDefault("c3_30_benke", defaultV).getRate());
        analysisTemplate.setC3_5_2(c3Map.getOrDefault("c3_40_benke", defaultV).getRate());
        analysisTemplate.setC3_6_2(c3Map.getOrDefault("c3_less_50_benke", defaultV).getRate());

        analysisTemplate.setC3_1_3(c3Map.getOrDefault("c3_less_10_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC3_2_3(c3Map.getOrDefault("c3_10_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC3_3_3(c3Map.getOrDefault("c3_20_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC3_4_3(c3Map.getOrDefault("c3_30_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC3_5_3(c3Map.getOrDefault("c3_40_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC3_6_3(c3Map.getOrDefault("c3_less_50_yanjiusheng", defaultV).getNum());
        analysisTemplate.setC3_1_4(c3Map.getOrDefault("c3_less_10_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC3_2_4(c3Map.getOrDefault("c3_10_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC3_3_4(c3Map.getOrDefault("c3_20_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC3_4_4(c3Map.getOrDefault("c3_30_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC3_5_4(c3Map.getOrDefault("c3_40_yanjiusheng", defaultV).getRate());
        analysisTemplate.setC3_6_4(c3Map.getOrDefault("c3_less_50_yanjiusheng", defaultV).getRate());

        analysisTemplate.setC3_1_5(analysisTemplate.getC3_1_3() + analysisTemplate.getC3_1_1());
        analysisTemplate.setC3_2_5(analysisTemplate.getC3_2_3() + analysisTemplate.getC3_2_1());
        analysisTemplate.setC3_3_5(analysisTemplate.getC3_3_3() + analysisTemplate.getC3_3_1());
        analysisTemplate.setC3_4_5(analysisTemplate.getC3_4_3() + analysisTemplate.getC3_4_1());
        analysisTemplate.setC3_5_5(analysisTemplate.getC3_5_3() + analysisTemplate.getC3_5_1());
        analysisTemplate.setC3_6_5(analysisTemplate.getC3_6_3() + analysisTemplate.getC3_6_1());
        analysisTemplate.setC3_1_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC3_1_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC3_2_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC3_2_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC3_3_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC3_3_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC3_4_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC3_4_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC3_5_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC3_5_5() / analysisTemplate.getCoverPartyMemberCount())));
        analysisTemplate.setC3_6_6(analysisTemplate.getCoverPartyMemberCount() == 0 ? 0 : Double.parseDouble(decimalFormat.format(1.0 * analysisTemplate.getC3_6_5() / analysisTemplate.getCoverPartyMemberCount())));


        InputStream inputStream = ResourceUtil.getStreamSafe("template/东北大学学生党员述责测评结果统计表.xlsx");

        EasyExcel.write(response.getOutputStream())
                .autoCloseStream(false)
                .withTemplate(inputStream)
                .sheet()
                .doFill(analysisTemplate);
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(dicCollegeDO.getCollegeName() + "学生党员述责测评结果统计表.xls", "UTF-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");

    }

}