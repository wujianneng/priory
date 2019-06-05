package com.pos.priory.utils;

import android.text.format.Time;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by hft on 2017/8/18.
 */

public class DateUtils {
    public static int getCurrentMonth(){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }

    private static boolean isC(String startTime, String endTime) {
        List<String> start_list = Arrays.asList(startTime.split(":"));
        List<String> end_list = Arrays.asList(endTime.split(":"));
        int start_h = Integer.parseInt(start_list.get(0));
        int start_m = Integer.parseInt(start_list.get(1));
        int end_h = Integer.parseInt(end_list.get(0));
        int end_m = Integer.parseInt(end_list.get(1));
        Calendar cal = Calendar.getInstance();// 当前日期
        int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
        int minute = cal.get(Calendar.MINUTE);// 获取分钟
        int minuteOfDay = hour * 60 + minute;
        final int start = start_h * 60 + start_m;
        final int end = end_h * 60 + end_m;
        if (minuteOfDay >= start && minuteOfDay <= end) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否过期
     *
     * @param startdate
     * @param enddate
     * @return
     */
    public static boolean isExpired(String startdate, String enddate) {
        Log.e("sys", "startdate:" + startdate + " enddate:" + enddate);
        boolean isexpired = true;
        long startTime = convertTimeToLong(startdate);
        long endTime = convertTimeToLong(enddate);
        long nowTime = System.currentTimeMillis();
        Log.e("sys", "startTime:" + startTime + " endTime:" + endTime + " nowTime:" + nowTime);
        if (nowTime >= startTime && nowTime <= endTime) {
            isexpired = false;
        }
        return isexpired;
    }

    /**
     * 比较两个日期的大小，日期格式为yyyy-MM-dd
     *
     * @param str1 pos机的时间
     * @param str2
     * @return true <br/>false
     */
    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    /**
     * 转换时间日期格式字串为long型
     *
     * @param time 格式为：yyyy-MM-dd HH:mm:ss的时间日期类型
     */
    public static Long convertTimeToLong(String time) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static String covertIso8601ToDate(String isotime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = formatter.parse(isotime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String sDate = sdf.format(date);
            return sDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String covertIso8601ToDate2(String isotime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = formatter.parse(isotime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String sDate = sdf.format(date);
            return sDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getTomorrowDay(String today) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();//获取日历实例
        try {
            calendar.setTime(sdf.parse(today));
        } catch (Exception e) {
            e.getMessage();
        }
        calendar.add(Calendar.DATE, 1);  //设置为后一天
        return sdf.format(calendar.getTime());//获得后一天
    }


    /**
     * 获取当期日期
     */
    public static String getToday() {
        String pattern = "yyyy/MM/dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }

    /**
     * 获取昨天
     */
    public static String getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date d = calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(d);// 获取昨天日期

    }

    public static String getLastMonthFirstDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return format.format(calendar.getTime());
    }

    public static String getLastMonthLastDay() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return sf.format(calendar.getTime());
    }

    public static String getThisMonthFirstDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cale = null;
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return format.format(cale.getTime());
    }

    public static String getLastWeekMondayDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        cal.add(Calendar.DATE, -7);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(cal.getTime());
    }

    public static String getLastWeekSundayDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(cal.getTime());
    }

    public static String getThisWeekMondayDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(cal.getTime());
    }

    /**
     * 获取前几天或者后几天的日期
     */
    public static String getBeforeOrAfterDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        Date d = calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(d);

    }

    /**
     * 获取前几天或者后几天的日期的时间戳
     */
    public static long getBeforeOrAfterDayTime(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        return calendar.getTime().getTime();
    }


    /**
     * 字符串日期格式的计算
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String smdate, String bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取据当前日期指定的日期
     */
    public static List<String> getSpecifyDates(int endDate) {
        List<String> dates = new ArrayList<String>();
        String pattern = "yyyy/MM/dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        Date today = new Date(); // 以当前
        Date beforeDate = null;
        Calendar calendar = Calendar.getInstance();
        String result = "";

        for (int i = 0; i < endDate; i++) {
            calendar.setTime(today);
            calendar.add(Calendar.DATE, -i);
            beforeDate = calendar.getTime();
            result = dateFormat.format(beforeDate);
            dates.add(result);
        }
        return dates;
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecond(Long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }


    /**
     * 判断指定日期是星期几
     */
    public static String getDayOfWeek(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayOfWeek = 0;
        String result = "";
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayOfWeek = 7;
        } else {
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }

        switch (dayOfWeek) {
            case 1:
                result = "星期一";
                break;
            case 2:
                result = "星期二";
                break;
            case 3:
                result = "星期三";
                break;
            case 4:
                result = "星期四";
                break;
            case 5:
                result = "星期五";
                break;
            case 6:
                result = "星期六";
                break;
            case 7:
                result = "星期日";
                break;
        }

        return result;
    }

    /**
     * 获取当前的日期
     * 这里的"格式"我们可以随意的指定，这里指定为 "yyyy-MM-dd-HH-mm",输出如下：
     * ****************** result :
     */

    public static String getDateOfToday() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String result = mSimpleDateFormat.format(new Date());

        return result;
    }

    public static String getDateByLongTime(long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public static String getHourByLongTime(long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public static String getMinByLongTime(long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }


    /**
     * 获取当前的时间<br>
     */

    public static String getHour() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH");
        String result = mSimpleDateFormat.format(new Date());
        return result;
    }


    public static String getMin() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("mm");
        String result = mSimpleDateFormat.format(new Date());
        return result;
    }

    /**
     * 获取当前的时间<br>
     */

    public static String getCurrentTime() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String result = mSimpleDateFormat.format(new Date());
        return result;
    }

    public static String getCurrentOrderNumber() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String result = mSimpleDateFormat.format(new Date());
        return result;
    }

    /**
     * 获取当前的时间<br>
     */

    public static String getCurrentHMSTime() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String result = mSimpleDateFormat.format(new Date());
        return result;
    }


    /**
     * 判断当前系统时间是否在指定时间的范围内
     *
     * @param beginHour 开始小时，例如22
     * @param beginMin  开始小时的分钟数，例如30
     * @param endHour   结束小时，例如 8
     * @param endMin    结束小时的分钟数，例如0
     * @return true表示在范围内，否则false
     */
    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        boolean result = false;
        final long aDayInMillis = 1000 * 60 * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();

        Time now = new Time();
        now.set(currentTimeMillis);

        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;

        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;

        if (!startTime.before(endTime)) {
// 跨天的特殊情况（比如22:00-8:00）
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
// 普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }


}
