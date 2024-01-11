package cn.iocoder.yudao.module.game.service.console.clients.dst.model;

import lombok.Data;

@Data
public class SessionMeta {

    private Integer index; // 存档节点索引, 从0开始, c_rollback(0), 回退到最近一次存档.
    private Integer number; // 前端显示第n个存档

    String fileName; // 0000000057
    String fileContent; // return {clock={totaltimeinphase=210,cycles=13,phase=\"day\",remainingtimeinphase=209.00875854492,mooomphasecycle=14,segs={night=3,day=7,dusk=6}},seasons={premode=false,season=\"autumn\",elapseddaysinseason=13,israndom={summer=false,autumn=false,spring=false,winter=false},lengths={summer=15,autumn=20,spring=20,winter=15},remainingdaysinseason=7,mode=\"cycle\",totaldaysinseason=40,segs={summer={night=4,day=11,dusk=1},autumn={night=2,day=8,dusk=6},spring={night=3,day=5,dusk=8},winter={night=6,day=5,dusk=5}}}}
    String masterSessionId; // 77E5AA2C90B251C3
    String cavesSessionId; // 84AFEC029036E18E

    Integer cycles;    // 已完成13个循环
    String currentDay;    // 第13+1天
    String season;    // 季节   summer=15,autumn=20,spring=20,winter=15
    String phase;     // day/dusk/night
    String phase_zh;  // 白天
    String phaseRate; // 第14天已过20%

}
