package cn.iocoder.yudao.module.game.common.response;

import lombok.Data;

@Data
public class DstBackupResponse {

    // 形如 /root/dst-backup/20221211215034
    private String dirPath;

    // 还原的目录大小 981M
    private String dirSize;

    // 还原时间 2023-01-15 21:19:36
    private String createTime;

}
