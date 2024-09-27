package com.thx.resourcelib.utils;

import com.thx.resourcelib.R;
import com.thx.resourcelib.base.BaseApplication;
import com.thx.resourcelib.ext.ResourceExtKt;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Description: 好用的时间格式化工具呢
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2023/6/3 2:59 PM
 */
public class TimeHelp {

    public static String format(long timeMillis) {
        return format(new Date(timeMillis));
    }

    private static String format(Date date) {
        Calendar calendar = Calendar.getInstance();
        //当前年
        int currYear = calendar.get(Calendar.YEAR);
        //当前日
        int currDay = calendar.get(Calendar.DAY_OF_YEAR);
        //当前时
        int currHour = calendar.get(Calendar.HOUR_OF_DAY);
        //当前分
        int currMinute = calendar.get(Calendar.MINUTE);
        //当前秒
        int currSecond = calendar.get(Calendar.SECOND);

        calendar.setTime(date);
        int msgYear = calendar.get(Calendar.YEAR);
        //说明不是同一年
        if (currYear != msgYear) {
            return new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(date);
        }
        int msgDay = calendar.get(Calendar.DAY_OF_YEAR);
        //超过7天，直接显示xx月xx日
        if (currDay - msgDay > 7) {
            return new SimpleDateFormat("MM/dd", Locale.getDefault()).format(date);
        }
        //不是当天
        if (currDay - msgDay > 0) {
            if (currDay - msgDay == 1) {
                return getString(R.string.im_chat_msg_yesterday);
            } else {
                return currDay - msgDay + getString(R.string.im_chat_msg_days_ago);
            }
        }
        int msgHour = calendar.get(Calendar.HOUR_OF_DAY);
        int msgMinute = calendar.get(Calendar.MINUTE);
        //不是当前小时内
        if (currHour - msgHour > 0) {
            //如果当前分钟小，说明最后一个不满一小时
            if (currMinute < msgMinute) {
                if (currHour - msgHour == 1) {//当前只大一个小时值，说明不够一小时
                    return 60 - msgMinute + currMinute + getString(R.string.im_chat_msg_minutes_ago);
                } else {
                    return currHour - msgHour - 1 + getString(R.string.im_chat_msg_hours_ago);
                }
            }
            //如果当前分钟数大，够了一个周期
            return currHour - msgHour + getString(R.string.im_chat_msg_hours_ago);
        }
        int msgSecond = calendar.get(Calendar.SECOND);
        //不是当前分钟内
        if (currMinute - msgMinute > 0) {
            //如果当前秒数小，说明最后一个不满一分钟
            if (currSecond < msgSecond) {
                if (currMinute - msgMinute == 1) {//当前只大一个分钟值，说明不够一分钟
                    return getString(R.string.im_chat_msg_now);
                } else {
                    return currMinute - msgMinute - 1 + getString(R.string.im_chat_msg_minutes_ago);
                }
            }
            //如果当前秒数大，够了一个周期
            return currMinute - msgMinute + getString(R.string.im_chat_msg_minutes_ago);
        }
        //x秒前
        return getString(R.string.im_chat_msg_now);
    }

    private static String getString(int resID) {
        return ResourceExtKt.getResourceString(BaseApplication.Companion.getInstance().getApplicationContext(), resID);
    }

}
