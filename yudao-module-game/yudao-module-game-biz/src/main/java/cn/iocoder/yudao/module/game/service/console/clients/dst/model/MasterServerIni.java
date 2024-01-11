package cn.iocoder.yudao.module.game.service.console.clients.dst.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterServerIni {

    private NETWORK NETWORK;
    private SHARD SHARD;
    private ACCOUNT ACCOUNT;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor    public static class NETWORK {
        private Integer server_port; // 10999
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor    public static class SHARD {
        private Boolean is_master;  // true/false
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor    public static class ACCOUNT {
        private Boolean encode_user_path;  // true/false
    }

}
