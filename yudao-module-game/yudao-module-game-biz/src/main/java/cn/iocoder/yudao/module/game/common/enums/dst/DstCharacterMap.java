package cn.iocoder.yudao.module.game.common.enums.dst;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DstCharacterMap {

    public static Map<String, Character> map = new HashMap<>() {{
        put("wilson", new Character("威尔逊", "wilson"));
        put("willow", new Character("薇洛", "willow"));
        put("wolfgang", new Character("沃尔夫冈", "wolfgang"));
        put("wendy", new Character("温蒂", "wendy"));
        put("wx78", new Character("WX-78(机器人)", "wx78"));
        put("wickerbottom", new Character("薇克巴顿", "wickerbottom"));
        put("woodie", new Character("伍迪", "woodie"));
        put("wes", new Character("韦斯", "wes"));
        put("waxwell", new Character("麦斯威尔", "WaxWell"));
        put("wathgrithr", new Character("薇格弗德(女武神)", "Wigfrid"));
        put("webber", new Character("韦伯(蜘蛛人)", "webber"));
        put("winona", new Character("薇诺娜", "winona"));
        put("warly", new Character("沃利", "warly"));
        put("wortox", new Character("沃拓克斯", "wortox"));
        put("wormwood", new Character("沃姆伍德", "wormwood"));
        put("wurt", new Character("沃特", "wurt"));
        put("walter", new Character("沃尔特", "walter"));
        put("wanda", new Character("旺达", "wanda"));
    }};


    @Data
    @AllArgsConstructor
    public static class Character {

        private String chineseName;
        private String englishName;

    }

}
