package com.cdkj.baselibrary.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author lsb
 * @date 2012-5-29 下午11:31:09
 */
public class DateUtil {
    public static final String DEFAULT_DATE_FMT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_MMddHHmm = "MM-dd HH:mm";

    public static final String DATE_YMD = "yyyy-MM-dd";

    public static final String DATE_TEMPLATE1 = "yyyy/MM/dd";


    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
                .format(c.getTime());
        return dayAfter;
    }

    /**
     * date1比date2更新（更迟，更晚），返回true，否则返回false
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isNewer(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        long d1 = date1.getTime();
        long d2 = date2.getTime();
        return d1 > d2;
    }

    /**
     * date1等于date2 或更新（更迟，更晚），返回true，否则返回false
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isNewer2(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        long d1 = date1.getTime();
        long d2 = date2.getTime();
        return d1 >= d2;
    }

    /**
     * 大于等于今天返回true
     *
     * @param
     * @param
     * @return
     */
    public static boolean isNewer2(int day1, int month, Calendar now) {
        if (day1 == 0 || now == null) {
            return false;
        }
        int d1 = now.get(Calendar.DAY_OF_MONTH);
        int d2 = now.get(Calendar.MONTH) + 1;
        return (day1 >= d1 && month >= d2) || month > d2;
    }

    public static Date parse(String input, String fmt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            return sdf.parse(input);
        } catch (ParseException e) {

        }

        SimpleDateFormat sdf1 = new SimpleDateFormat(fmt);
        try {

            return sdf1.parse("1970-01-01 00:00");

        } catch (ParseException e) {
        }

        return new Date();
    }

    public static Date parseNoException(String input) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FMT);
            return sdf.parse(input);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date parse(String input) {
        return parse(input, DATE_YMD);
    }


    public static String format(Date date, String fmt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            return sdf.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static String format(String date, String fmt) {
        try {
            Date date1 = new Date(date);
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            return sdf.format(date1);
        } catch (Exception e) {
            return "";
        }
    }

    public static String format(Date date) {
        return format(date, DEFAULT_DATE_FMT);
    }


    public static long getAlarmTime() {
        Date date = new Date();
        return date.getTime() + 1000 * 30;
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getWeekOfDate(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy年MM月dd日");
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(fmt.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    //根据日期取得星期几
    public static String getWeekString(Date date) {
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    //获取两个data 之间的 data集合
    public static List getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List lDate = new ArrayList();
        lDate.add(beginDate);//把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        //使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            //根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后士大夫
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);//把结束时间加入集合
        return lDate;
    }

    //得到后两个月的日期
    public static Date getMonthToDate(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        Date formNow3Month = calendar.getTime();
        return formNow3Month;
    }

    /**
     * 获取 当前日期几天后 的日期用于显示
     *
     * @param day
     * @return
     */
    public static String getShowDayToData(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        Date formNow3Month = calendar.getTime();
        return format(formNow3Month, DATE_YMD);
    }

    public static String getCurrentDate() {
        return format(new Date(), "yyyy-MM-dd");
    }


    public static String formatStringData(String s, String format) {
        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(format)) {
            return "";
        }

        return DateUtil.format(new Date(s), format);
    }

    public static String formatIntegerData(int s, String format) {
        if (s == 0 || TextUtils.isEmpty(format)) {
            return "";
        }

        return DateUtil.format(new Date(s), format);
    }

    public static Date parseStringData(String s, String format) {
        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(format)) {
            return null;
        }

        return DateUtil.parse(s, format);
    }

    /**
     * 获取一个月的最大的天数
     */
    public static int getDaysOfMonth(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取两个时间的差
     *
     * @param d1 大的时间（距离现在更近的时间）
     * @param d2 小的时间（距离现在更远的时间）
     * @return
     */
    public static int getDateDValue(Date d1, Date d2) {
        long diff;
        long hours;
        long days = 0;
        long minutes = 0;
        long totalMinutes = 0;
        try {
            diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            totalMinutes = diff / (1000 * 60);
            days = diff / (1000 * 60 * 60 * 24);
            hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            System.out.println("" + days + "天" + hours + "小时" + minutes + "分,总共" + totalMinutes + "分钟、");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (int) totalMinutes;
    }

    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    /**
     * 格式化秒数
     *
     * @param seconds
     */
    public static String formatSeconds(int seconds) {

        String str = "";
        if (seconds > 60) {
            int fen = seconds / 60;
            if (fen > 60) {
                int shi = seconds / 60 / 60;
                int miao = seconds % 3600;
                if (miao > 60) {
                    str = "剩余付款时间：" + shi + "时" + miao / 60 + "分" + miao % 60 + "秒";
                } else {
                    str = "剩余付款时间：" + shi + "时" + "0分" + miao + "秒";
                }
            } else {
                str = "剩余付款时间：" + fen + "分" + seconds % 60 + "秒";
            }
        } else {
            str = "剩余付款时间：" + seconds + "秒";
        }
        return str;
    }

    /**
     * 返回当前时间的前一个月的  时间
     *
     * @param fmt 返回时间的格式
     * @return
     */
    public static String fristMonth(String fmt) {
        //过去一月
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = format(m, fmt);
        return mon;
    }

    /**
     * 返回当前时间的前一个月的  时间
     *
     * @param fmt 返回时间的格式
     * @param dayNumber 想减少几天或者增加几天的日期   正数为增加  负数为减少
     * @return
     */
    public static String fristDay(String fmt,int dayNumber) {
        //过去一月
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, dayNumber);
        Date m = c.getTime();
        String mon = format(m, fmt);
        return mon;
    }

}
