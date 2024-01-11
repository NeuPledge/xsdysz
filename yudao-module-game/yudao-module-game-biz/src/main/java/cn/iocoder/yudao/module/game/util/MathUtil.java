package cn.iocoder.yudao.module.game.util;

import java.math.BigDecimal;

public class MathUtil {

    /**
     * 除以1000，保留小数点后3位, 四舍五入.
     */
    public static double longRound3Up(Long a) {
        a = a == null ? 0 : a;
        BigDecimal b = new BigDecimal(a);
        double res = b.divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
        return res;
    }

    /**
     * 保留小数点后3位, 四舍五入.
     */
    public static double round3Up(double a) {
        try {
            BigDecimal b = new BigDecimal(a);
            a = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            return a;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 保留小数点后2位, 四舍五入.
     */
    public static double roundUp(double a, int scale) {
        try {
            BigDecimal b = new BigDecimal(a);
            a = b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
            return a;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 保留小数点后3位, 四舍五入.
     */
    public static double roundBigDecimal3Up(BigDecimal a) {
        double b = a.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        return b;
    }

    /**
     * 计算除法.
     */
    public static double divide(int a, int b) {
        if (b == 0) {
            return 0;
        }
        return round3Up(a / (double) b);
    }

    /**
     * 计算除法.
     */
    public static double divide(BigDecimal a, BigDecimal b) {
        if (b.intValue() == 0) {
            return 0;
        }
        return round3Up(a.intValue() / b.doubleValue());
    }

    /**
     * 计算除法.
     */
    public static double divide(long a, BigDecimal b) {
        if (b.intValue() == 0) {
            return 0;
        }
        return round3Up(a / b.doubleValue());
    }

    /**
     * 计算除法.
     */
    public static double divide(int a, long b) {
        if (b == 0) {
            return 0;
        }
        return round3Up(a / (double) b);
    }

    /**
     * 计算除法.
     */
    public static double divide(long a, long b) {
        if (b == 0) {
            return 0;
        }
        return round3Up(a / (double) b);
    }

    /**
     * 计算除法.
     */
    public static double divide(int a, double b) {
        if (b == 0) {
            return 0;
        }
        return round3Up(a / (double) b);
    }
    /**
     * 计算除法.
     */
    public static double divide(double a, int b) {
        if (b == 0) {
            return 0;
        }
        return round3Up(a / (double) b);
    }

}
