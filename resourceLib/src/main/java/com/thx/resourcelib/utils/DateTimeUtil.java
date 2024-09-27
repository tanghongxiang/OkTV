package com.thx.resourcelib.utils;

import android.content.Context;

import com.thx.resourcelib.base.BaseApplication;
import com.thx.resourcelib.ext.ResourceExtKt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


public class DateTimeUtil {

    private final static long minute = 60 * 1000;
    private final static long hour = 60 * minute;
    private final static long day = 24 * hour;
    private final static long week = 7 * day;
    private final static long month = 31 * day;
    private final static long year = 12 * month;

    /**
     * return format text for time
     * you can see https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html
     * today：HH:MM
     * this week：Sunday, Friday ..
     * this year：MM/DD
     * before this year：YYYY/MM/DD
     *
     * @param date current time
     * @return format text
     */
    public static String getTimeFormatText(Date date) {
        if (date == null || date.getTime() == 0) {
            return "";
        }
        return getTimeStringAutoShort2(date, true);
        // 以下是腾讯IM代码，在部分机型上因为缺少语言包，时间格式显示不正确
//        Context context = TUIConfig.getAppContext();
//        Locale locale;
//        if (context == null) {
//            locale = Locale.getDefault();
//        } else {
//            locale = TUIThemeManager.getInstance().getLocale(context);
//        }
//        String timeText;
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        long dayStartTimeInMillis = calendar.getTimeInMillis();
//        calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_WEEK, 1);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        long weekStartTimeInMillis = calendar.getTimeInMillis();
//        calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_YEAR, 1);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        long yearStartTimeInMillis = calendar.getTimeInMillis();
//        long outTimeMillis = date.getTime();
//        if (outTimeMillis < yearStartTimeInMillis) {
//            timeText = String.format(locale, "%tD", date);
//        } else if (outTimeMillis < weekStartTimeInMillis) {
//            timeText = String.format(locale, "%1$tm/%1$td", date);
//        } else if (outTimeMillis < dayStartTimeInMillis) {
//            timeText = String.format(locale, "%tA", date);
//        } else {
//            timeText = String.format(locale, "%tR", date);
//        }
//        return timeText;
    }

    public static String formatSecondsTo00(int timeSeconds) {
        int second = timeSeconds % 60;
        int minuteTemp = timeSeconds / 60;
        if (minuteTemp > 0) {
            int minute = minuteTemp % 60;
            int hour = minuteTemp / 60;
            if (hour > 0) {
                return (hour >= 10 ? (hour + "") : ("0" + hour)) + ":" + (minute >= 10 ? (minute + "") : ("0" + minute))
                        + ":" + (second >= 10 ? (second + "") : ("0" + second));
            } else {
                return (minute >= 10 ? (minute + "") : ("0" + minute)) + ":"
                        + (second >= 10 ? (second + "") : ("0" + second));
            }
        } else {
            return "00:" + (second >= 10 ? (second + "") : ("0" + second));
        }
    }

    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 返回指定pattern样的日期时间字符串。
     *
     * @param dt
     * @param pattern
     * @return 如果时间转换成功则返回结果，否则返回空字符串""
     * @author 即时通讯网([url = http : / / www.52im.net]http : / / www.52im.net[ / url])
     */
    public static String getTimeString(Date dt, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);//"yyyy-MM-dd HH:mm:ss"
            sdf.setTimeZone(TimeZone.getDefault());
            return sdf.format(dt);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 仿照微信中的消息时间显示逻辑，将时间戳（单位：毫秒）转换为友好的显示格式.
     * <p>
     * 1）7天之内的日期显示逻辑是：今天、昨天(-1d)、前天(-2d)、星期？（只显示总计7天之内的星期数，即<=-4d）；<br>
     * 2）7天之外（即>7天）的逻辑：直接显示完整日期时间。
     *
     * @param srcDate         要处理的源日期时间对象
     * @param mustIncludeTime true表示输出的格式里一定会包含“时间:分钟”，否则不包含（参考微信，不包含时分的情况，用于首页“消息”中显示时）
     * @return 输出格式形如：“10:30”、“昨天 12:04”、“前天 20:51”、“星期二”、“2019/2/21 12:09”等形式
     * @author 即时通讯网([url = http : / / www.52im.net]http : / / www.52im.net[ / url])
     * @since 4.5
     */
    public static String getTimeStringAutoShort2(Date srcDate, boolean mustIncludeTime) {
        String ret = "";

        try {
            GregorianCalendar gcCurrent = new GregorianCalendar();
            gcCurrent.setTime(new Date());
            int currentYear = gcCurrent.get(GregorianCalendar.YEAR);
            int currentMonth = gcCurrent.get(GregorianCalendar.MONTH) + 1;
            int currentDay = gcCurrent.get(GregorianCalendar.DAY_OF_MONTH);

            GregorianCalendar gcSrc = new GregorianCalendar();
            gcSrc.setTime(srcDate);
            int srcYear = gcSrc.get(GregorianCalendar.YEAR);
            int srcMonth = gcSrc.get(GregorianCalendar.MONTH) + 1;
            int srcDay = gcSrc.get(GregorianCalendar.DAY_OF_MONTH);

            // 要额外显示的时间分钟
            String timeExtraStr = (mustIncludeTime ? " " + getTimeString(srcDate, "HH:mm") : "");

            // 当年
            if (currentYear == srcYear) {
                long currentTimestamp = gcCurrent.getTimeInMillis();
                long srcTimestamp = gcSrc.getTimeInMillis();

                // 相差时间（单位：毫秒）
                long delta = (currentTimestamp - srcTimestamp);

                // 当天（月份和日期一致才是）
                if (currentMonth == srcMonth && currentDay == srcDay) {
                    // 时间相差60秒以内
                    if (delta < 60 * 1000)
                        ret = getString(com.thx.resourcelib.R.string.im_chat_msg_now);//"刚刚";
                        // 否则当天其它时间段的，直接显示“时:分”的形式
                    else
                        ret = getTimeString(srcDate, "HH:mm");
                }
                // 当年 && 当天之外的时间（即昨天及以前的时间）
                else {
                    // 昨天（以“现在”的时候为基准-1天）
                    GregorianCalendar yesterdayDate = new GregorianCalendar();
                    yesterdayDate.add(GregorianCalendar.DAY_OF_MONTH, -1);

                    // 前天（以“现在”的时候为基准-2天）
                    GregorianCalendar beforeYesterdayDate = new GregorianCalendar();
                    beforeYesterdayDate.add(GregorianCalendar.DAY_OF_MONTH, -2);

                    // 用目标日期的“月”和“天”跟上方计算出来的“昨天”进行比较，是最为准确的（如果用时间戳差值
                    // 的形式，是不准确的，比如：现在时刻是2019年02月22日1:00、而srcDate是2019年02月21日23:00，
                    // 这两者间只相差2小时，直接用“delta/(3600 * 1000)” > 24小时来判断是否昨天，就完全是扯蛋的逻辑了）
                    if (srcMonth == (yesterdayDate.get(GregorianCalendar.MONTH) + 1)
                            && srcDay == yesterdayDate.get(GregorianCalendar.DAY_OF_MONTH)) {
//                        ret = "昨天" + timeExtraStr;// -1d
                        ret = getString(com.thx.resourcelib.R.string.im_chat_msg_yesterday) + timeExtraStr;
                    }
                    // “前天”判断逻辑同上
//                    else if (srcMonth == (beforeYesterdayDate.get(GregorianCalendar.MONTH) + 1)
//                            && srcDay == beforeYesterdayDate.get(GregorianCalendar.DAY_OF_MONTH)) {
//                        ret = "前天" + timeExtraStr;// -2d
//                    }
                    else {
                        // 跟当前时间相差的小时数
                        long deltaHour = (delta / (3600 * 1000));

                        // 如果小于 7*24小时就显示星期几
                        if (deltaHour < 7 * 24) {
                            String[] weekday = {
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_sunday),
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_monday),
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_tuesday),
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_wednesday),
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_thursday),
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_friday),
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_saturday)
                            };

                            // 取出当前是星期几
                            String weedayDesc = weekday[gcSrc.get(GregorianCalendar.DAY_OF_WEEK) - 1];
                            ret = weedayDesc + timeExtraStr;
                        }
                        // 否则直接显示完整日期时间
                        else
                            ret = getTimeString(srcDate, "MM/dd") + timeExtraStr;
                    }
                }
            } else
                ret = getTimeString(srcDate, "yyyy/MM/dd日") + timeExtraStr;
        } catch (Exception e) {
            System.err.println("【DEBUG-getTimeStringAutoShort】计算出错：" + e.getMessage() + " 【NO】");
        }

        return ret;
    }

    /**
     * 仿照微信中的消息时间显示逻辑，将时间戳（单位：毫秒）转换为友好的显示格式.
     * <p>
     * 1）7天之内的日期显示逻辑是：今天、昨天(-1d)、前天(-2d)、星期？（只显示总计7天之内的星期数，即<=-4d）；<br>
     * 2）7天之外（即>7天）的逻辑：直接显示完整日期时间。
     *
     * @param srcDate         要处理的源日期时间对象
     * @param mustIncludeTime true表示输出的格式里一定会包含“时间:分钟”，否则不包含（参考微信，不包含时分的情况，用于首页“消息”中显示时）
     * @return 输出格式形如：“10:30”、“昨天 12:04”、“前天 20:51”、“星期二”、“2019/2/21 12:09”等形式
     * @author 即时通讯网([url = http : / / www.52im.net]http : / / www.52im.net[ / url])
     * @since 4.5
     */
    public static String getTimeStringAutoShortForList(Date srcDate) {
        String ret = "";

        try {
            GregorianCalendar gcCurrent = new GregorianCalendar();
            gcCurrent.setTime(new Date());
            int currentYear = gcCurrent.get(GregorianCalendar.YEAR);
            int currentMonth = gcCurrent.get(GregorianCalendar.MONTH) + 1;
            int currentDay = gcCurrent.get(GregorianCalendar.DAY_OF_MONTH);

            GregorianCalendar gcSrc = new GregorianCalendar();
            gcSrc.setTime(srcDate);
            int srcYear = gcSrc.get(GregorianCalendar.YEAR);
            int srcMonth = gcSrc.get(GregorianCalendar.MONTH) + 1;
            int srcDay = gcSrc.get(GregorianCalendar.DAY_OF_MONTH);

            // 要额外显示的时间分钟
            String timeExtraStr = " " + getTimeString(srcDate, "HH:mm");

            // 当年
            if (currentYear == srcYear) {
                long currentTimestamp = gcCurrent.getTimeInMillis();
                long srcTimestamp = gcSrc.getTimeInMillis();

                // 相差时间（单位：毫秒）
                long delta = (currentTimestamp - srcTimestamp);

                // 当天（月份和日期一致才是）
                if (currentMonth == srcMonth && currentDay == srcDay) {
                    // 时间相差60秒以内
                    if (delta < 60 * 1000)
                        ret = getString(com.thx.resourcelib.R.string.im_chat_msg_now);//"刚刚";
                        // 否则当天其它时间段的，直接显示“时:分”的形式
                    else
                        ret = getTimeString(srcDate, "HH:mm");
                }
                // 当年 && 当天之外的时间（即昨天及以前的时间）
                else {
                    // 昨天（以“现在”的时候为基准-1天）
                    GregorianCalendar yesterdayDate = new GregorianCalendar();
                    yesterdayDate.add(GregorianCalendar.DAY_OF_MONTH, -1);

                    // 前天（以“现在”的时候为基准-2天）
                    GregorianCalendar beforeYesterdayDate = new GregorianCalendar();
                    beforeYesterdayDate.add(GregorianCalendar.DAY_OF_MONTH, -2);

                    // 用目标日期的“月”和“天”跟上方计算出来的“昨天”进行比较，是最为准确的（如果用时间戳差值
                    // 的形式，是不准确的，比如：现在时刻是2019年02月22日1:00、而srcDate是2019年02月21日23:00，
                    // 这两者间只相差2小时，直接用“delta/(3600 * 1000)” > 24小时来判断是否昨天，就完全是扯蛋的逻辑了）
                    if (srcMonth == (yesterdayDate.get(GregorianCalendar.MONTH) + 1)
                            && srcDay == yesterdayDate.get(GregorianCalendar.DAY_OF_MONTH)) {
//                        ret = "昨天" + timeExtraStr;// -1d
                        ret = getString(com.thx.resourcelib.R.string.im_chat_msg_yesterday) + timeExtraStr;
                    }
                    // “前天”判断逻辑同上
//                    else if (srcMonth == (beforeYesterdayDate.get(GregorianCalendar.MONTH) + 1)
//                            && srcDay == beforeYesterdayDate.get(GregorianCalendar.DAY_OF_MONTH)) {
//                        ret = "前天" + timeExtraStr;// -2d
//                    }
                    else {
                        // 跟当前时间相差的小时数
                        long deltaHour = (delta / (3600 * 1000));

                        // 如果小于 7*24小时就显示星期几
                        if (deltaHour < 7 * 24) {
                            String[] weekday = {
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_sunday),
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_monday),
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_tuesday),
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_wednesday),
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_thursday),
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_friday),
                                    getString(com.thx.resourcelib.R.string.im_chat_msg_saturday)
                            };

                            // 取出当前是星期几
                            String weedayDesc = weekday[gcSrc.get(GregorianCalendar.DAY_OF_WEEK) - 1];
                            ret = weedayDesc;
                        }
                        // 否则直接显示完整日期时间
                        else
                            ret = getTimeString(srcDate, "MM/dd");
                    }
                }
            } else
                ret = getTimeString(srcDate, "yyyy/MM/dd");
        } catch (Exception e) {
            System.err.println("【DEBUG-getTimeStringAutoShort】计算出错：" + e.getMessage() + " 【NO】");
        }

        return ret;
    }

    private static String getString(int resID) {
        return ResourceExtKt.getResourceString(BaseApplication.Companion.getInstance().getApplicationContext(), resID);
    }

}
