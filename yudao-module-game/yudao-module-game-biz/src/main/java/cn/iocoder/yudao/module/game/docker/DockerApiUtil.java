package cn.iocoder.yudao.module.game.docker;

import com.alibaba.fastjson.JSON;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DockerApiUtil {


    public static DockerClient getDockerClient() {
        // 进行安全认证
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                // 服务器ip
                .withDockerHost("tcp://101.42.44.78:2375")
                .withDockerTlsVerify(true)
                .withDockerCertPath("D:\\projects2023\\pandacloudgame\\yudao-module-game\\yudao-module-game-biz\\src\\main\\resources\\certs-docker")
                .build();
        // docker命令执行工厂
        DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory()
                .withReadTimeout(6000)
                .withConnectTimeout(6000)
                .withMaxTotalConnections(100)
                .withMaxPerRouteConnections(10);
        DockerClient dockerClient = DockerClientBuilder.getInstance(config).withDockerCmdExecFactory(dockerCmdExecFactory).build();
        return dockerClient;
    }


    public static void main(String[] args) {
        DockerClient dockerClient = DockerApiUtil.getDockerClient();

        List<Container> containers = dockerClient.listContainersCmd().exec();

        log.info(JSON.toJSONString(containers, true));
    }

}
