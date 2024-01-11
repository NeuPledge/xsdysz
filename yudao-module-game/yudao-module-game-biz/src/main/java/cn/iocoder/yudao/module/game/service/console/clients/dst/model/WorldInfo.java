package cn.iocoder.yudao.module.game.service.console.clients.dst.model;

import lombok.Data;

/**
 * 世界信息.
 */
@Data
public class WorldInfo {
    private Clock clock;
    private Seasons seasons;

    @Data
    public static class Clock {
        private int totaltimeinphase;
        private int cycles; // 当前游戏天数. 25
        private String phase;// 当前阶段, 白天day
        private double remainingtimeinphase;
        private int mooomphasecycle;// 距离月圆天数
        private Segs segs;

        @Data
        public static class Segs {
            private int night;
            private int day;
            private int dusk;
        }
    }

    @Data
    public static class Seasons {
        private boolean premode;
        private String season; // 当前季节
        private int elapseddaysinseason;// 当前季节过去天数
        private Israndom israndom;
        private Lengths lengths;
        private int remainingdaysinseason; // 当前季节剩余天数
        private String mode;
        private int totaldaysinseason;
        private Segs segs;

        @Data
        public static class Israndom {
            private boolean summer;
            private boolean autumn;
            private boolean spring;
            private boolean winter;
        }

        @Data
        public static class Lengths {
            private int summer;
            private int autumn;
            private int spring;
            private int winter;

        }

        @Data
        public static class Segs {
            private Summer summer;
            private Autumn autumn;
            private Spring spring;
            private Winter winter;

            @Data
            public static class Summer {
                private int night;
                private int day;
                private int dusk;

            }

            @Data
            public static class Autumn {
                private int night;
                private int day;
                private int dusk;

            }

            @Data
            public static class Spring {
                private int night;
                private int day;
                private int dusk;

            }

            @Data
            public static class Winter {
                private int night;
                private int day;
                private int dusk;

            }
        }
    }
}


