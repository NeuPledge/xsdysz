package cn.iocoder.yudao.module.debrief.service.impl;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.debrief.config.DebriefConfig;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.UnCommentStudent;
import cn.iocoder.yudao.module.debrief.controller.app.vo.PartyMemberCommentData;
import cn.iocoder.yudao.module.debrief.controller.app.vo.PartyMemberResultData;
import cn.iocoder.yudao.module.debrief.dal.dataobject.diccollege.DicCollegeDO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicgrade.DicGradeDO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.partymember.PartyMemberDO;
import cn.iocoder.yudao.module.debrief.dal.mysql.diccollege.DicCollegeMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicgrade.DicGradeMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.evaluateresult.EvaluateResultMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.partymember.PartyMemberMapper;
import cn.iocoder.yudao.module.debrief.service.DicService;
import cn.iocoder.yudao.module.debrief.service.FileService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DebriefFileServiceImpl implements FileService {

    @Resource
    private PartyMemberMapper partyMemberMapper;

    @Resource
    private DicCollegeMapper dicCollegeMapper;

    @Resource
    private DicGradeMapper dicGradeMapper;

    @Resource
    private EvaluateResultMapper evaluateResultMapper;

    @Resource
    private DicService dicCacheService;

/*    @Value("${debrief.download-path}")
    public String downloadPath;

    @Value("${debrief.download-comment-path}")
    public String downloadCommentPath;

    @Value("${debrief.download-enable}")
    public Boolean downloadEnable;*/

    @Autowired
    private DebriefConfig debriefConfig;

    // // @Scheduled("${server.port}")
    // public void aggregateData(){
    //
    // }

    public File getCollegeFile(String fileName, List<PartyMemberResultData> partyMembers) {
        // 获取桌面路径
        FileSystemView fsv = FileSystemView.getFileSystemView();
        String desktop = fsv.getHomeDirectory().getPath();
        // String filePath = desktop + File.separator + "debrief_data" + File.separator + fileName;
        String filePath = debriefConfig.getDownloadPath() + File.separator + fileName;

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("党员述责统计结果");
        HSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("党员数据库索引");
        header.createCell(1).setCellValue("姓名");
        header.createCell(2).setCellValue("学号");
        header.createCell(3).setCellValue("学院");
        header.createCell(4).setCellValue("支部");
        header.createCell(5).setCellValue("班级");
        header.createCell(6).setCellValue("满意数");
        header.createCell(7).setCellValue("基本满意数");
        header.createCell(8).setCellValue("不满意数");

        for (int i = 0; i < partyMembers.size(); i++) {
            HSSFRow row = sheet.createRow(i + 1);
            PartyMemberResultData member = partyMembers.get(i);
            row.createCell(0).setCellValue(member.getId());
            row.createCell(1).setCellValue(member.getName());
            row.createCell(2).setCellValue(member.getStudentNumber());
            row.createCell(3).setCellValue(member.getCollegeName());
            row.createCell(4).setCellValue(member.getBranchName());
            row.createCell(5).setCellValue(member.getGradeName());
            Long satisfaction = member.getSatisfaction();
            row.createCell(6).setCellValue(Objects.isNull(satisfaction) ? 0 : satisfaction);
            Long baseSatisfaction = member.getBaseSatisfaction();
            row.createCell(7).setCellValue(Objects.isNull(baseSatisfaction) ? 0 : baseSatisfaction);
            Long dissatisfaction = member.getDissatisfaction();
            row.createCell(8).setCellValue(Objects.isNull(dissatisfaction) ? 0 : dissatisfaction);
        }
        workbook.setActiveSheet(0);
        File file = new File(filePath);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new ServiceException("excel保存路径未找到");
        }
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new ServiceException("数据写到文件时出错");
        }
        return file;

    }

    public File getCollegeComment(String fileName, List<PartyMemberCommentData> partyMembers) {
        // 获取桌面路径
        FileSystemView fsv = FileSystemView.getFileSystemView();
        String desktop = fsv.getHomeDirectory().getPath();
        // String filePath = desktop + File.separator + "debrief_data" + File.separator + "comment" + File.separator + fileName;
        String filePath = debriefConfig.getDownloadCommentPath() + File.separator + fileName;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("党员述责其他评价意见");
        HSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("党员数据库索引");
        header.createCell(1).setCellValue("姓名");
        header.createCell(2).setCellValue("学号");
        header.createCell(3).setCellValue("学院");
        header.createCell(4).setCellValue("支部");
        header.createCell(5).setCellValue("班级");
        header.createCell(6).setCellValue("其他评价");

        for (int i = 0; i < partyMembers.size(); i++) {
            HSSFRow row = sheet.createRow(i + 1);
            PartyMemberCommentData member = partyMembers.get(i);
            row.createCell(0).setCellValue(member.getId());
            row.createCell(1).setCellValue(member.getName());
            row.createCell(2).setCellValue(member.getStudentNumber());
            row.createCell(3).setCellValue(member.getCollegeName());
            row.createCell(4).setCellValue(member.getBranchName());
            row.createCell(5).setCellValue(member.getGradeName());
            String content = member.getContent();
            row.createCell(6).setCellValue(Objects.isNull(content) ? "" : content);
        }
        workbook.setActiveSheet(0);
        File file = new File(filePath);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new ServiceException("excel保存路径未找到");
        }
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            throw new ServiceException("数据写到文件时出错");
        }
        return file;

    }

    public ResponseEntity<FileSystemResource> export(File file, String fileName) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        try {
            headers.add("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(), "iso-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ServiceException("不支持的编码格式");
        }
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity.ok().headers(headers).contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream")).body(new FileSystemResource(file));
    }

/*    @Override
    public ResponseEntity<FileSystemResource> getCollegeFileIm(Long collegeId) {
        DicCollege dicCollege = dicCollegeMapper.selectByPrimaryKey(collegeId);
        if (Objects.isNull(dicCollege)) {
            throw new ServiceException("查询的学院不存在");
        }
        List<PartyMemberResultData> partyMembers = partyMemberMapper.selectResultByCollegeIdIm(collegeId);
        String fileName = dicCollege.getId() + "_party_member_votes.xls";
        return export(getCollegeFile(fileName, partyMembers), fileName);
    }*/

    /**
     * 实时获取学校的党员述责结果.
     *
     * @param collegeId 学院id.
     */
    @Override
    public void getCollegeFileImSharding(Long collegeId, HttpServletResponse response) {

        if (!debriefConfig.getDownloadEnable()) {
            return;
        }

        DicCollegeDO dicCollege = dicCollegeMapper.selectById(collegeId);
        if (Objects.isNull(dicCollege)) {
            throw new ServiceException("查询的学院不存在");
        }


        List<PartyMemberDO> partyMembers = partyMemberMapper.selectList(new LambdaQueryWrapperX<PartyMemberDO>().eq(PartyMemberDO::getCollegeId, collegeId));
        List<Map<String, Long>> satisfaction = evaluateResultMapper.groupByCount(1, collegeId);
        List<Map<String, Long>> baseSatisfaction = evaluateResultMapper.groupByCount(2, collegeId);
        List<Map<String, Long>> disSatisfaction = evaluateResultMapper.groupByCount(3, collegeId);
        List<DicGradeDO> dicGrades = dicGradeMapper.selectList(new LambdaQueryWrapperX<DicGradeDO>().eq(DicGradeDO::getCollegeId, collegeId).orderByAsc(DicGradeDO::getGradeName));

        String collegeName = dicCollege.getCollegeName();
        Map<Long, String> idMapGradeName = new HashMap<>();
        for (DicGradeDO dicGrade : dicGrades) {
            idMapGradeName.put(dicGrade.getId(), dicGrade.getGradeName());
        }

        List<PartyMemberResultData> partyMemberResultDataList = new ArrayList<>();
        for (PartyMemberDO partyMember : partyMembers) {
            PartyMemberResultData partyMemberResultData = new PartyMemberResultData();
            for (Map<String, Long> longIntegerMap : satisfaction) {
                if (longIntegerMap.get("party_member_id").equals(partyMember.getId())) {
                    partyMemberResultData.setSatisfaction(longIntegerMap.get("comment"));
                }
            }
            for (Map<String, Long> longIntegerMap : baseSatisfaction) {
                if (longIntegerMap.get("party_member_id").equals(partyMember.getId())) {
                    partyMemberResultData.setBaseSatisfaction(longIntegerMap.get("comment"));
                }
            }
            for (Map<String, Long> longIntegerMap : disSatisfaction) {
                if (longIntegerMap.get("party_member_id").equals(partyMember.getId())) {
                    partyMemberResultData.setDissatisfaction(longIntegerMap.get("comment"));
                }
            }
            String branchName = dicCacheService.getBranchName(partyMember.getBranchId());
            partyMemberResultData.setBranchName(branchName);
            partyMemberResultData.setCollegeName(collegeName);
            partyMemberResultData.setGradeName(idMapGradeName.get(partyMember.getGradeId()));

            partyMemberResultData.setId(partyMember.getId());
            partyMemberResultData.setStudentNumber(partyMember.getStudentNumber());
            partyMemberResultData.setName(partyMember.getName());

            partyMemberResultDataList.add(partyMemberResultData);
        }

//        String fileName = dicCollege.getId() + "_party_member_sharding_votes.xls";

        // 导出 Excel
        try {
            ExcelUtils.write(response, dicCollege.getCollegeName() + ".xls", "数据", PartyMemberResultData.class,
                    partyMemberResultDataList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        return export(getCollegeFile(fileName, partyMemberResultDataList), fileName);
    }

    /**
     * 实时获取学校的党员其他评价内容.
     *
     * @param collegeId 学院id.
     */
    @Override
    public void getCollegeCommentSharding(Long collegeId, HttpServletResponse response) {

        if (!debriefConfig.getDownloadEnable()) {
            return;
        }

        DicCollegeDO dicCollege = dicCollegeMapper.selectById(collegeId);
        if (Objects.isNull(dicCollege)) {
            throw new ServiceException("查询的学院不存在");
        }
        String collegeName = dicCollege.getCollegeName();
        List<PartyMemberDO> partyMembers = partyMemberMapper.selectList(new LambdaQueryWrapperX<PartyMemberDO>().eq(PartyMemberDO::getCollegeId, collegeId));

        Map<Long, PartyMemberDO> partyMemberMap = new HashMap<>();
        for (PartyMemberDO partyMember : partyMembers) {
            partyMemberMap.put(partyMember.getId(), partyMember);
        }
        List<DicGradeDO> dicGrades = dicGradeMapper.selectList(new LambdaQueryWrapperX<DicGradeDO>().eq(DicGradeDO::getCollegeId, collegeId).orderByAsc(DicGradeDO::getGradeName));
        Map<Long, String> idMapGradeName = new HashMap<>();
        for (DicGradeDO dicGrade : dicGrades) {
            idMapGradeName.put(dicGrade.getId(), dicGrade.getGradeName());
        }
        List<Map<String, Object>> memberMapContent = partyMemberMapper
                .selectCollegeCommentByCollegeIdSharding(collegeId);
        List<PartyMemberCommentData> partyMemberCommentDatas = new ArrayList<>();

        for (Map<String, Object> stringObjectMap : memberMapContent) {
            Long partyMemberId = (Long) stringObjectMap.get("party_member_id");
            PartyMemberDO partyMember = partyMemberMap.get(partyMemberId);
            PartyMemberCommentData partyMemberCommentData = new PartyMemberCommentData();
            partyMemberCommentData.setId(partyMemberId);
            String branchName = dicCacheService.getBranchName(partyMember.getBranchId());
            partyMemberCommentData.setBranchName(branchName);
            partyMemberCommentData.setCollegeName(collegeName);
            partyMemberCommentData.setContent((String) stringObjectMap.get("content"));
            partyMemberCommentData.setGradeName(idMapGradeName.get(partyMember.getGradeId()));
            partyMemberCommentData.setStudentNumber(partyMember.getStudentNumber());
            partyMemberCommentData.setName(partyMember.getName());
            partyMemberCommentDatas.add(partyMemberCommentData);
        }
//        String fileName = dicCollege.getId() + "_comments_sharding.xls";
//        return export(getCollegeComment(fileName, partyMemberCommentDatas), fileName);

        try {
            ExcelUtils.write(response, dicCollege.getCollegeName() + ".xls", "数据", PartyMemberCommentData.class,
                    partyMemberCommentDatas);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /*
    @Override
    public ResponseEntity<FileSystemResource> getAllCollegesFileIm() {
        List<PartyMemberResultData> partyMembers = partyMemberMapper.selectResultIm();
        String fileName = "all_colleges_votes.xls";
        return export(getCollegeFile(fileName, partyMembers), fileName);
    }
    */

/*    @Override
    public ResponseEntity<FileSystemResource> getCollegeComment(Long collegeId) {
        DicCollege dicCollege = dicCollegeMapper.selectByPrimaryKey(collegeId);
        if (Objects.isNull(dicCollege)) {
            throw new ServiceException("查询的学院不存在");
        }
        List<PartyMemberCommentData> partyMemberCommentDatas = partyMemberMapper.
            selectCollegeCommentByCollegeId(collegeId);
        String fileName = dicCollege.getId() + "_other_comments.xls";
        return export(getCollegeComment(fileName, partyMemberCommentDatas), fileName);
    }*/

/*    @Override
    public ResponseEntity<FileSystemResource> getAllCollegesComment() {
        List<PartyMemberCommentData> partyMembers = partyMemberMapper.selectCollegeComment();
        String fileName = "all_colleges_other_comments.xls";
        return export(getCollegeComment(fileName, partyMembers), fileName);
    }*/
}
