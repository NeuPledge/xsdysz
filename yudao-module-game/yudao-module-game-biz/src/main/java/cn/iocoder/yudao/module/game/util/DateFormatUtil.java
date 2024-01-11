package cn.iocoder.yudao.module.game.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class DateFormatUtil {

    public enum Pattern {
        _1("yyyy-MM-dd HH:mm:ss"),
        _2("yyyy-MM-dd"),
        _3("yyyyMMdd"),
        _4("yyyyMMddHHmmss"),
        _41("yyyy年MM月dd日HH时mm分ss秒"),
        _5("HH:mm:ss"),
        _6("MM-dd"),
        _19("yyyy-MM-dd HH:00"),// 拉取渠道小时数据用
        _20("yyyy-MM-dd HH:mm"),// 拉取渠道小时数据用
        _START_TIME("yyyy-MM-dd 00:00:00"),// 开始时间
        _END_TIME("yyyy-MM-dd 23:59:59"),//  结束时间
        _STEAM_WORKSHOP("yyyy 年 MM-dd HH:mm:ss"),
        _ROOM_STAMP("MMddHH"), // 测试房间名称的时间戳   熊猫云服测试房间011923
        ;

        private final String pattern;

        Pattern(String pattern) {
            this.pattern = pattern;
        }

        public String getPattern() {
            return pattern;
        }
    }

    /**
     * 用于解析不带时间的字符串(入参随意, 但是返回的日期将丢失时间).
     */
    public static Date parseDate(String source, Pattern pattern) {
        LocalDate localDate = LocalDate.parse(source, DateTimeFormatter.ofPattern(pattern.getPattern()));
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 用于解析带时间的字符串.
     */
    public static Date parseDateTime(String source, Pattern pattern) {
        LocalDateTime localDateTime = LocalDateTime.parse(source, DateTimeFormatter.ofPattern(pattern.getPattern()));
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 返回结果不会包含任何的时间值, 只有日期值.
     */
    public static String formatDate(Date date, Pattern pattern) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern.getPattern());
        return dateTimeFormatter.format(localDate);
    }

    /**
     * 返回今天的日期, 只有日期值.
     */
    public static String formatDateToday(Pattern pattern) {
        Instant instant = new Date().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern.getPattern());
        return dateTimeFormatter.format(localDate);
    }

    /**
     * 返回以今天为基准的日期, 只有日期值.
     * 比如今天0512
     * days=1 返回0513
     * days=-1 返回0511
     */
    public static String formatDateDay(Pattern pattern, int days) {
        if (days == 0) {
            return formatDateToday(pattern);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        Instant instant = calendar.getTime().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern.getPattern());
        return dateTimeFormatter.format(localDate);
    }

    /**
     * 返回以今天为基准的日期, 只有日期值.
     * 比如今天0512
     * days=1 返回0513
     * days=-1 返回0511
     */
    public static String formatDateDay(Pattern pattern, int field, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(field, value);
        Instant instant = calendar.getTime().toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern.getPattern());
        return dateTimeFormatter.format(localDate);
    }

    /**
     * 从一个日期转为另一个日期样式.
     * 比如入参为2022-05-12, 返参可以是是20220512
     */
    public static String formatDateTranslation(String source, Pattern sourcePattern, Pattern targetPattern) {
        Date date = parseDate(source, sourcePattern);
        return formatDate(date, targetPattern);
    }

    /**
     * 返回结果包含时间值和日期值.
     */
    public static String formatDateTime(Date date, Pattern pattern) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern.getPattern());
        return dateTimeFormatter.format(localDateTime);
    }

    /**
     * 获取给定日期的凌晨时间.
     * 比如传入 2022-08-10 10:01:13, 返回2022-08-10 00:00:00
     */
    public static Date getDayStart(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return instance.getTime();
    }
    /**
     * 获取给定日期的最后时刻.
     * 比如传入 2022-08-10 10:01:13, 返回2022-08-10 23:59:59
     */
    public static Date getDayEnd(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);
        return instance.getTime();
    }

    public static void main(String[] args) {
        Date dayStart = getDayStart(new Date());
        System.out.println(dayStart);
    }
}
