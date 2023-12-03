package cn.iocoder.yudao.module.debrief.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(DebriefConfig.PREFIX)
public class DebriefConfig {

    public static final String PREFIX = "debrief";

    private Boolean downloadEnable;

    private String downloadPath;

    private String downloadCommentPath;

}
