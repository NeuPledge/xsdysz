package cn.iocoder.yudao.module.debrief.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;


public interface ErrorCodeConstants {

    ErrorCode DIC_BRANCH_NOT_EXISTS = new ErrorCode(2_001_001_001, "支部字典列不存在");
    ErrorCode DIC_COLLEGE_NOT_EXISTS = new ErrorCode(2_001_001_001, "支部字典列不存在");
    ErrorCode EVALUATE_RESULT_NOT_EXISTS = new ErrorCode(2_001_001_001, "支部字典列不存在");
    ErrorCode DIC_GRADE_NOT_EXISTS = new ErrorCode(2_001_001_001, "支部字典列不存在");
    ErrorCode PARTY_MEMBER_NOT_EXISTS = new ErrorCode(2_001_001_001, "支部字典列不存在");
    ErrorCode STUDENT_NOT_EXISTS = new ErrorCode(2_001_001_001, "支部字典列不存在");

    ErrorCode IMPORT_DATA_ERROR = new ErrorCode(2_002_001_001, "导入数据解析出错");
}