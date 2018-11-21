package com.grumpycat.hotpot.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by cc.he on 2018/9/28
 */
public class TimeUtil {
    public static String getCurrentTime(){
        long ms = System.currentTimeMillis();
        Date date =new Date(ms);
        Calendar gc = new GregorianCalendar();
        gc.setTime(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(gc.getTime());
    }
}
