package cn.iocoder.yudao.module.debrief.config;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author sunchenchen
 */
@Getter
@Setter
@Configuration
public class ApplicationConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

    @Value("${debrief.name}")
    public String name;
    @Value("${server.port}")
    public Integer port;

    @Value("${debrief.download-path-windows}")
    public String downloadPathWindows;

    @Value("${debrief.download-path-linux}")
    public String downloadPathLinux;

    @Value("${debrief.download-comment-path-windows}")
    public String downloadCommentPathWindows;

    @Value("${debrief.download-comment-path-linux}")
    public String downloadCommentPathLinux;

    @Value("${debrief.download-enable}")
    public Boolean downloadEnable;

    @Resource
    private DebriefConfig debriefConfig;

    public void check() {

        try {
            LOGGER.debug(name + "已启动");
            LOGGER.debug(String.format("doc 访问地址: http://%s:%d/doc.html",
                InetAddress.getLocalHost().getHostName(), port));
            LOGGER.debug(String.format("swagger 访问地址: http://%s:%d/swagger-ui.html",
                InetAddress.getLocalHost().getHostName(), port));
            LOGGER.debug(String.format("actuator 监控地址: http://%s:%d/actuator",
                InetAddress.getLocalHost().getHostName(), port));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        String osName = System.getProperty("os.name").toUpperCase();
        OsType osType = OsType.getType(osName);
        LOGGER.info("osName:{}", osName);

        // 检查三次.
        if (downloadEnable) {
            debriefConfig.setDownloadEnable(downloadEnable);
            switch (osType) {
                case WINDOWS:
                    debriefConfig.setDownloadPath(downloadPathWindows);
                    debriefConfig.setDownloadCommentPath(downloadCommentPathWindows);
                    break;
                case LINUX:
                default:
                    debriefConfig.setDownloadPath(downloadPathLinux);
                    debriefConfig.setDownloadCommentPath(downloadCommentPathLinux);
            }

            checkDownloadPath();
            checkDownloadPath();
            checkDownloadPath();
        }
        LOGGER.info(" Done !");
    }

    public void checkDownloadPath() {
        Path pathDownload = Paths.get(debriefConfig.getDownloadPath());
        if (!pathDownload.toFile().exists()) {
            try {
                LOGGER.info("{}路径不存在，将自动创建", debriefConfig.getDownloadPath());
                Files.createDirectory(pathDownload);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Path pathComment = Paths.get(debriefConfig.getDownloadCommentPath());
        if (!pathComment.toFile().exists()) {
            try {
                LOGGER.info("{}路径不存在，将自动创建", debriefConfig.getDownloadCommentPath());
                Files.createDirectory(pathComment);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
