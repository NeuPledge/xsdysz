package cn.iocoder.yudao.module.game.service.console.clients.dst.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CavesServerIni {

    private NETWORK NETWORK;
    private SHARD SHARD;
    private ACCOUNT ACCOUNT;
    private STEAM STEAM;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class STEAM {
        private Integer master_server_port; // 27017
        private Integer authentication_port; // 8767
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NETWORK {
        private Integer server_port; // 10998
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SHARD {
        private Boolean is_master;  // true/false
        private String name;  // Caves
        private String id;  // 1392047842
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ACCOUNT {
        private Boolean encode_user_path;  // true/false
    }

}
