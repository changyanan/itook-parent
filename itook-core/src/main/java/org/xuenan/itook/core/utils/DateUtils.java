package org.xuenan.itook.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public abstract class DateUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    private static final Map<String, LinkedList<SimpleDateFormat>> simpleDateFormats = new ConcurrentSkipListMap();
    private static long stime = System.currentTimeMillis() / 1000L;
    private static final String[] dateFormats = new String[]{"yyyy-MM-dd HH:mm:ss.S", "yyyy-MM-dd HH:mm:ss.SS", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH", "yyyy-MM-dd", "yyyy-MM", "yyyy/MM/dd HH:mm:ss.S", "yyyy/MM/dd HH:mm:ss.SS", "yyyy/MM/dd HH:mm:ss.SSS", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH", "yyyy/MM/dd", "yyyy/MM", "yyyy.MM.dd HH:mm:ss.S", "yyyy.MM.dd HH:mm:ss.SS", "yyyy.MM.dd HH:mm:ss.SSS", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM.dd HH", "yyyy.MM.dd", "yyyy.MM"};
    private static final SimpleDateFormat yMdHm = getDateFormat("y年M月d日H时m分");
    private static final SimpleDateFormat ymd = getDateFormat("y年M月d日");
    private static final SimpleDateFormat md = getDateFormat("M月d日");

    public DateUtils() {
    }

    public static final void register(String... patterns) {
        String[] var1 = patterns;
        int var2 = patterns.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String pattern = var1[var3];
            if (pattern != null) {
                pattern = pattern.trim();
                releaseDateFormat(pattern, getDateFormat(pattern));
            }
        }

    }

    private static synchronized SimpleDateFormat getDateFormat(String pattern) {
        return simpleDateFormats.containsKey(pattern) && !((LinkedList)simpleDateFormats.get(pattern)).isEmpty() ? (SimpleDateFormat)((LinkedList)simpleDateFormats.get(pattern)).pop() : new SimpleDateFormat(pattern);
    }

    private static synchronized void releaseDateFormat(String pattern, SimpleDateFormat dateFormat) {
        if (!simpleDateFormats.containsKey(pattern)) {
            simpleDateFormats.put(pattern, new LinkedList());
        }

        ((LinkedList)simpleDateFormats.get(pattern)).push(dateFormat);
    }

    public static final Date initToDayBegin(Date date) {
        if (date == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(11, 0);
            cal.set(12, 0);
            cal.set(13, 0);
            cal.set(14, 0);
            return cal.getTime();
        }
    }

    public static final Date initToMonthBegin(Date date) {
        if (date == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(5, 1);
            cal.set(11, 0);
            cal.set(12, 0);
            cal.set(13, 0);
            cal.set(14, 0);
            return cal.getTime();
        }
    }

    public static final Date initToDayEnd(Date date) {
        if (date == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(11, 23);
            cal.set(12, 59);
            cal.set(13, 59);
            cal.set(14, 999);
            return cal.getTime();
        }
    }

    public static final Date parse(String source) {
        if (StringUtils.isEmpty(new String[]{source})) {
            return null;
        } else {
            source = source.trim();
            Set<String> keys = simpleDateFormats.keySet();
            Iterator var2 = keys.iterator();

            while(true) {
                String pattern;
                do {
                    do {
                        do {
                            do {
                                if (!var2.hasNext()) {
                                    Long time = NumberUtils.parseLong(source);
                                    if (time == null) {
                                        return null;
                                    }

                                    if (stime < time) {
                                        return new Date(time);
                                    }

                                    return new Date(time * 1000L);
                                }

                                pattern = (String)var2.next();
                            } while(pattern.length() != source.length());
                        } while(pattern.indexOf(45) != source.indexOf(45));
                    } while(pattern.indexOf(46) != source.indexOf(46));
                } while(pattern.indexOf(47) != source.indexOf(47));

                SimpleDateFormat dateFormat = getDateFormat(pattern);

                try {
                    Date var5 = dateFormat.parse(source);
                    return var5;
                } catch (Exception var9) {
                    logger.debug("参数{}转化为日期{}失败", source, pattern);
                } finally {
                    releaseDateFormat(pattern, dateFormat);
                }
            }
        }
    }

    public static final String format(Integer dateInt, String pattern) {
        return format(int2Date(dateInt), pattern);
    }

    public static final String formatByLong(Long dateLong, String pattern) {
        return null == dateLong ? null : format(int2Date((int)(dateLong / 1000L)), pattern);
    }

    public static final String format(Date date, String pattern) {
        if (date == null) {
            return null;
        } else if (StringUtils.isEmpty(new String[]{pattern})) {
            return format(date);
        } else {
            SimpleDateFormat dateFormat = getDateFormat(pattern);

            String var3;
            try {
                var3 = dateFormat.format(date);
            } finally {
                releaseDateFormat(pattern, dateFormat);
            }

            return var3;
        }
    }

    public static final String format(Date date) {
        if (date == null) {
            return null;
        } else {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = getDateFormat(pattern);

            String var3;
            try {
                var3 = dateFormat.format(date);
            } finally {
                releaseDateFormat(pattern, dateFormat);
            }

            return var3;
        }
    }

    public static final Integer date2Int(Date date) {
        return null == date ? null : (int)(date.getTime() / 1000L);
    }

    public static final Date int2Date(Integer dateInt) {
        return null == dateInt ? null : new Date((new BigInteger(dateInt.toString())).multiply(new BigInteger("1000")).longValue());
    }

    public static final int getYearByDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(1);
    }

    public static final Date addYear(Date date, int year) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(1, year);
        return c.getTime();
    }

    public static final Date addMonth(Date date, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(2, month);
        return c.getTime();
    }

    public static Date addSecond(Date date, long second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(13, (int)second);
        return calendar.getTime();
    }

    public static final Date getWeekStart(Integer week) {
        Date d = new Date();
        int date = d.getDate() - d.getDay() + (week == null ? 0 : week * 7);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(5, date);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static final Date getWeekEnd(Integer week) {
        Date d = new Date();
        int date = d.getDate() - d.getDay() + (week == null ? 0 : week * 7) + 7;
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(5, date);
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        cal.set(14, 99);
        return cal.getTime();
    }

    public static String beforeNow(Date date) {
        if (date == null) {
            return null;
        } else {
            long between_count = (System.currentTimeMillis() - date.getTime()) / 1000L;
            if (between_count < -60L) {
                return yMdHm.format(date);
            } else if (between_count < 60L) {
                return "刚刚";
            } else if ((between_count /= 60L) < 60L) {
                return between_count + "分钟前";
            } else if ((between_count /= 60L) < 24L) {
                return between_count + "小时前";
            } else if ((between_count /= 24L) == 1L) {
                return "昨天";
            } else if (between_count == 2L) {
                return "前天";
            } else if (between_count < 28L) {
                return between_count + "天前";
            } else {
                return date.getYear() == (new Date()).getYear() ? md.format(date) : ymd.format(date);
            }
        }
    }

    public static final long getIntervalDays(long beginTime, long endTime) {
        return (endTime - beginTime) / 86400L;
    }

    public static final String addDay(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(5, days);
        return format(c.getTime(), "yyyyMMdd");
    }

    public static final long getTimestamp(int days) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(5, days);
        return c.getTimeInMillis() / 1000L;
    }

    public static final int intervalDays(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate).getDays();
    }

    public static void main(String[] args) {
        System.err.println(parse("2019-09-08 22:12:30.200"));
    }

    static {
        register(dateFormats);
    }
}
