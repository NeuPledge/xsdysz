package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AnalysisTemplate {


    private String year;

    private String collegeName;

    private String dateymd;

    @Schema(description = "学生党员人数")
    private Integer allPartyMemberCount;
    @Schema(description = "参加述责党员人数")
    private Integer coverPartyMemberCount;
    @Schema(description = "参加测评学生人数")
    private Integer coverStudentCount;
    @Schema(description = "本科生述责党员数")
    private Integer benKePartyMemberCount;
    @Schema(description = "研究生述责党员数")
    private Integer yanJiuShengPartyMemberCount;


    private Integer c1_1_1;
    private Integer c1_2_1;
    private Integer c1_3_1;
    private Integer c1_4_1;
    private Integer c1_5_1;
    private Integer c1_6_1;
    private Integer c1_7_1;
    private Double c1_1_2;
    private Double c1_2_2;
    private Double c1_3_2;
    private Double c1_4_2;
    private Double c1_5_2;
    private Double c1_6_2;
    private Double c1_7_2;
    private Integer c1_1_3;
    private Integer c1_2_3;
    private Integer c1_3_3;
    private Integer c1_4_3;
    private Integer c1_5_3;
    private Integer c1_6_3;
    private Integer c1_7_3;
    private Double c1_1_4;
    private Double c1_2_4;
    private Double c1_3_4;
    private Double c1_4_4;
    private Double c1_5_4;
    private Double c1_6_4;
    private Double c1_7_4;
    private Integer c1_1_5;
    private Integer c1_2_5;
    private Integer c1_3_5;
    private Integer c1_4_5;
    private Integer c1_5_5;
    private Integer c1_6_5;
    private Integer c1_7_5;
    private Double c1_1_6;
    private Double c1_2_6;
    private Double c1_3_6;
    private Double c1_4_6;
    private Double c1_5_6;
    private Double c1_6_6;
    private Double c1_7_6;


    private Integer c12_1_1;
    private Integer c12_2_1;
    private Integer c12_3_1;
    private Integer c12_4_1;
    private Integer c12_5_1;
    private Integer c12_6_1;
    private Integer c12_7_1;
    private Double c12_1_2;
    private Double c12_2_2;
    private Double c12_3_2;
    private Double c12_4_2;
    private Double c12_5_2;
    private Double c12_6_2;
    private Double c12_7_2;
    private Integer c12_1_3;
    private Integer c12_2_3;
    private Integer c12_3_3;
    private Integer c12_4_3;
    private Integer c12_5_3;
    private Integer c12_6_3;
    private Integer c12_7_3;
    private Double c12_1_4;
    private Double c12_2_4;
    private Double c12_3_4;
    private Double c12_4_4;
    private Double c12_5_4;
    private Double c12_6_4;
    private Double c12_7_4;
    private Integer c12_1_5;
    private Integer c12_2_5;
    private Integer c12_3_5;
    private Integer c12_4_5;
    private Integer c12_5_5;
    private Integer c12_6_5;
    private Integer c12_7_5;
    private Double c12_1_6;
    private Double c12_2_6;
    private Double c12_3_6;
    private Double c12_4_6;
    private Double c12_5_6;
    private Double c12_6_6;
    private Double c12_7_6;


    private Integer c3_1_1;
    private Integer c3_2_1;
    private Integer c3_3_1;
    private Integer c3_4_1;
    private Integer c3_5_1;
    private Integer c3_6_1;
    private Double c3_1_2;
    private Double c3_2_2;
    private Double c3_3_2;
    private Double c3_4_2;
    private Double c3_5_2;
    private Double c3_6_2;
    private Integer c3_1_3;
    private Integer c3_2_3;
    private Integer c3_3_3;
    private Integer c3_4_3;
    private Integer c3_5_3;
    private Integer c3_6_3;
    private Double c3_1_4;
    private Double c3_2_4;
    private Double c3_3_4;
    private Double c3_4_4;
    private Double c3_5_4;
    private Double c3_6_4;
    private Integer c3_1_5;
    private Integer c3_2_5;
    private Integer c3_3_5;
    private Integer c3_4_5;
    private Integer c3_5_5;
    private Integer c3_6_5;
    private Double c3_1_6;
    private Double c3_2_6;
    private Double c3_3_6;
    private Double c3_4_6;
    private Double c3_5_6;
    private Double c3_6_6;

}
