package cn.iocoder.yudao.module.game.service.console.clients.dst.model;

import java.util.HashMap;
import lombok.Data;

@Data
public class MasterLevelOverride {

    private String desc;
    private String hideminimap;
    private String id;
    private String location;
    private String max_playlist_position;
    private String min_playlist_position;
    private String name;
    private String numrandom_set_pieces;
    private String override_level_string;
    private HashMap<String, Object> overrides;
    private String random_set_pieces;
    private String required_prefabs;
    private String required_setpieces;
    private String settings_desc;
    private String settings_id;
    private String settings_name;
    private String substitutes;
    private String version;
    private String worldgen_desc;
    private String worldgen_id;
    private String worldgen_name;

}
