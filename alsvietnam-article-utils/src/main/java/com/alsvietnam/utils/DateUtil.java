package com.alsvietnam.utils;

import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 11:33 PM
 */

@UtilityClass
@ExtensionMethod(Extensions.class)
@Slf4j
public class DateUtil {

    public final static String MINUTE = "minute";
    public final static String HOURLY = "hourly";
    public final static String DAILY = "daily";
    public final static String MONTHLY = "monthly";
    public final static String YEARLY = "yearly";
    public final static String TYPE_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public final static String TYPE_FORMAT_2 = "yyyy-MM-dd";
    public final static String TYPE_FORMAT_3 = "yyyyMMddHHmmss";

    public Date customDate(Date fromDate) {
        if (null != fromDate) {
            SimpleDateFormat sdfd = new SimpleDateFormat("dd");
            int day = Integer.parseInt(sdfd.format(fromDate));
            SimpleDateFormat sdfm = new SimpleDateFormat("MM");
            int month = Integer.parseInt(sdfm.format(fromDate));
            SimpleDateFormat sdfy = new SimpleDateFormat("yyyy");
            int year = Integer.parseInt(sdfy.format(fromDate));

            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            fromDate = cal.getTime();
        }
        return fromDate;
    }

    public int getNumberDaysBetween2Dates(Date start, Date end) {
        if (null == start || null == end) return 0;
        start = customDate(start);
        end = customDate(end);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(start);
        c2.setTime(end);
        double noDay = 1.0 * (c2.getTime().getTime() - c1.getTime().getTime()) / (24 * 3600 * 1000);
        noDay = Math.round(noDay);
        return (int) noDay;
    }

    public Date getDateAfterNumberTimes(Date date, int number, String type) {
        Calendar cal = Calendar.getInstance();
        if (null != date) {
            cal.setTimeInMillis(date.getTime());
        }
        if (type.equalsIgnoreCase(MINUTE)) {
            cal.add(Calendar.MINUTE, +number);
        } else if (type.equalsIgnoreCase(HOURLY)) {
            cal.add(Calendar.HOUR, +number);
        } else if (type.equalsIgnoreCase(DAILY)) {
            cal.add(Calendar.DAY_OF_YEAR, +number);
        } else if (type.equalsIgnoreCase(MONTHLY)) {
            cal.add(Calendar.MONTH, +number);
        } else if (type.equalsIgnoreCase(YEARLY)) {
            cal.add(Calendar.YEAR, +number);
        }
        return cal.getTime();
    }

    public Date getDateBeforeNumberTimes(Date date, int number, String type) {
        Calendar cal = Calendar.getInstance();
        if (null != date) {
            cal.setTimeInMillis(date.getTime());
        }
        if (type.equalsIgnoreCase(HOURLY)) {
            cal.add(Calendar.HOUR, -number);
        } else if (type.equalsIgnoreCase(DAILY)) {
            cal.add(Calendar.DAY_OF_YEAR, -number);
        } else if (type.equalsIgnoreCase(MONTHLY)) {
            cal.add(Calendar.MONTH, -number);
        } else if (type.equalsIgnoreCase(YEARLY)) {
            cal.add(Calendar.YEAR, -number);
        }
        return cal.getTime();
    }

    public String convertDateToString(Date date, String type) {
        if (null != date) {
            SimpleDateFormat sdf = new SimpleDateFormat(type);
            return sdf.format(date);
        }
        return "";
    }

    @SneakyThrows
    public Date formatDateString(String dateString, String type) {
        if (!dateString.isBlankOrNull()) {
            SimpleDateFormat sdf = new SimpleDateFormat(type, Locale.US);
            return sdf.parse(dateString);
        }
        return null;
    }


    public String formatDateString(String dateString, String patternFrom, String patternTo) {
        if (!dateString.isBlankOrNull()) {
            try {
                SimpleDateFormat sdfFrom = new SimpleDateFormat(patternFrom, Locale.US);
                SimpleDateFormat sdfTo = new SimpleDateFormat(patternTo, Locale.US);
                Date date = sdfFrom.parse(dateString);
                return sdfTo.format(date);
            } catch (ParseException e) {
                log.error("DateUtil - ConvertStringToDate error: ", e);
            }
        }
        return null;
    }

    public static Date customFromDate(Date fromDate) {
        return customFromDate(fromDate, true);
    }

    public static Date customFromDate(Date fromDate, boolean changeHour) {
        if (null != fromDate) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            if (changeHour) {
                cal.set(Calendar.HOUR_OF_DAY, 0);
            }
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            fromDate = cal.getTime();
        }
        return fromDate;
    }

    public static Date customToDate(Date toDate) {
        return customToDate(toDate, true);
    }

    public static Date customToDate(Date toDate, boolean changeHour) {
        if (null != toDate) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(toDate);
            if (changeHour) {
                cal.set(Calendar.HOUR_OF_DAY, 23);
            }
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            toDate = cal.getTime();
        }
        return toDate;
    }
}
