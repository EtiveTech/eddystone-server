/*
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 * Copyright: Etive Technologies Ltd. 2016 (www.etive.org). All rights reserved.
 * Created  : 13-Dec-2016
 * Author   : Craig, (c dot speedie at etive dot org)
 * Enquiries: please send any enquiries to hello at etive dot org
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */
package org.etive.city4age.withings.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * Utilities for use in City4Age API calls.
 * @author Craig
 */
public final class City4AgeDateUtils {

    private static final String CALENDAR_ERROR = "Method input must be an instance of java.util.Calendar";
    private static final String SQL_FORMAT = "yyyy-MM-dd";
    private static final String STRING_ERROR = "Method input must be an instance of String and have content other than spaces.";
    private static final int MAX_YEAR = 2037;

    /**
     * Get a {@code Calendar} instance from a date {@code String} in format &quot;yyyy-MM-dd&quot;.
     * @param date The date to parse.
     * @return A {@code Calendar} instance representing the {@code date} passed.
     */
    public static Calendar getCalendarFromSqlDateString(String date){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(SQL_FORMAT, Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(date));
        } catch (Exception e) {
            cal = null;
        }
        return cal;
    }

    /**
     * Get a {@code Calendar} instance from a date {@code String} in format &quot;yyyy-MM-dd&quot;.
     * @param date The date to parse.
     * @return A {@code String} instance representing the {@code date} passed.
     */
    public static String getSimpleDateString(Calendar cal){
        String out = "";
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd-MMM", Locale.ENGLISH);
        try {
            out = sdf.format(cal.getTime());
        } catch (Exception e) {
            // do nothing
        }
        return out;
    }

    /**
     * Get the date today and five days ahead in SQL format (&quot;yyyy-MM-dd&quot;).
     * @return A {@code Map<String, Calendar>} with keys &quot;now&quot; for today and &quot;then&quot;
     *         for five days ahead.
     */
    public static Map<String, Calendar> getTodayAndFiveAhead() {
        final int five = 5;
        Map<String, Calendar> days = new HashMap<>();
        Calendar now = new GregorianCalendar();
        Calendar fiveAhead = new GregorianCalendar();
        fiveAhead.add(Calendar.DAY_OF_MONTH, five);
        days.put("now", now);
        days.put("then", fiveAhead);
        return days;
    }

    /**
     * Convert a {@code Calendar} instance into unix epoch seconds.
     * @param aCalendar An instance of {@code Calendar}.
     * @return A {@code String} representation of {@code aCalendar} in Unix Epoch 32-bit format. The method
     *         contains a check to ensure only dates up to year 2037 are converted thus avoiding the Unix
     *         &quot;Y2K&quot; problem.
     * @throws IllegalArgumentException If {@code null == aCalendar} or the year is greater than 2037.
     */
    public static String getDateAsEpoch(final Calendar aCalendar) throws IllegalArgumentException {
        String out = "";
        if (null == aCalendar || aCalendar.get(Calendar.YEAR) > MAX_YEAR) {
            throw new IllegalArgumentException(CALENDAR_ERROR + " and before the end of year " + MAX_YEAR);
        } else {
            long seconds = aCalendar.getTimeInMillis()/1000L;
            out = Long.toUnsignedString(seconds);
            if (out.contains("\\.")) {
                out = out.split("\\.")[0];
            }
        }
        return out;
    }

    /**
     * Convert a {@code String} instance in format yyyy-MM-dd to unix epoch seconds.
     * @param aString An instance of {@code String}.
     * @return A {@code String} representation of {@code aString} in Unix Epoch 32-bit format. The method
     *         contains a check to ensure only dates up to year 2037 are converted thus avoiding the Unix
     *         &quot;Y2K&quot; problem.
     * @throws IllegalArgumentException If {@code aDate} is {@code null}, zero length or only whitespace
     *                                  or the year is found to be greater than 2037.
     */
    public static String getDateAsEpoch(final String aString) throws IllegalArgumentException {
        if (StringUtils.isBlank(aString)) {
            throw new IllegalArgumentException(STRING_ERROR);
        }
        if (Integer.parseInt(aString.substring(0,4)) > MAX_YEAR) {
            throw new IllegalArgumentException(CALENDAR_ERROR + " and before the end of year " + MAX_YEAR);
        }
        String out = "";
        Calendar aCalendar = Calendar.getInstance();
        String[] dateParts = aString.split("-");
        aCalendar.set(Integer.parseInt(dateParts[0]),
                      Integer.parseInt(dateParts[1]),
                      Integer.parseInt(dateParts[2]), 12, 0, 0);
        long seconds = aCalendar.getTimeInMillis()/1000L;
        out = Long.toUnsignedString(seconds);
        if (out.contains("\\.")) {
            out = out.split("\\.")[0];
        }
        return out;
    }

    /**
     * Converts a ten digit Unix Epoch datetime {@code String} into a date then splits the date into its
     * date and time parts.
     * @param epochDate A ten digit Unix epoch date.
     * @return A {@code Calendar} instance or {@code null} if parsing fails.
     * @throws IllegalArgumentException If {@code epochDate} is {@code null}, zero length or only whitespace.
     */
    public static Calendar convertEpochStringToCalendar(final String epochDate) throws IllegalArgumentException {
        if (StringUtils.isBlank(epochDate)) {
            throw new IllegalArgumentException(STRING_ERROR);
        }
        Calendar cal = Calendar.getInstance();
        long millis = Long.parseLong(epochDate) * 1000L;
        cal.setTimeInMillis(millis);
        return cal;
    }

    /**
     * Converts a ten digit Unix Epoch value to its date string representation in format &quot;dd-MM-yyyy&quot;.
     * @param epochValue
     * @return
     */
    public static String getDateStringFromEpoch(long epochValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(epochValue * 1000L);
    }

    /**
     * Switches a date {@code String} in format &quot;yyyy-MM-dd&quot; to &quot;dd-MM-yyyy&quot;.
     * @param aDate The date text to format.
     * @return A {@code String} representing a date in format &quot;dd-MM-yyyy&quot;.
     * @throws IllegalArgumentException If {@code aDate} is {@code null}, zero length or only whitespace.
     */
    public static String switchDatePartPositions(String aDate) throws IllegalArgumentException {
        if (StringUtils.isBlank(aDate)) {
            throw new IllegalArgumentException(STRING_ERROR);
        }
        final String separator = "-";
        String[] bits = StringUtils.isBlank(aDate) ? "0-0-0".split(separator) : aDate.split(separator);
        return bits[2] + separator + bits[1] + separator + bits[0];
    }

    /**
     * Convert a {@code Calendar} instance to format yyyy-MM-dd.
     * @param aCalendar An instance of {@code Calendar}.
     * @return A {@code String} representation of {@code aCalendar} in format &quot;yyyy-MM-dd&quot;.
     * @throws IllegalArgumentException If {@code null == aCalendar}.
     */
    public static String getDateInSqlFormat(final Calendar aCalendar) throws IllegalArgumentException {
        if (null == aCalendar) {
            throw new IllegalArgumentException(CALENDAR_ERROR);
        }
        DateFormat dateFormat = new SimpleDateFormat(SQL_FORMAT);
        return dateFormat.format(aCalendar.getTime());
    }

    /**
     * Convert a {@code Calendar} instance to format &quot;dd-MM-yyyy HH:mm&quot; .
     * @param aCalendar An instance of {@code Calendar}.
     * @return A {@code String} representation of {@code aCalendar} in format &quot;yyyy-MM-dd HH:mm&quot;.
     * @throws IllegalArgumentException If {@code null == aCalendar}.
     */
    public static String getDateInDateTimeFormat(final Calendar aCalendar) throws IllegalArgumentException {
        if (null == aCalendar) {
            throw new IllegalArgumentException(CALENDAR_ERROR);
        }
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return dateFormat.format(aCalendar.getTime());
    }

    /**
     * Convert a {@code String} instance of a Unix Epoch to format &quot;dd-MM-yyyy&quot; .
     * @param aString An instance of {@code String} containing ten digit characters.
     * @return A {@code String} representation of {@code aCalendar} in format &quot;yyyy-MM-dd&quot;.
     * @throws IllegalArgumentException If {@code null == aCalendar}.
     */
    public static String getEpochInDisplayFormat(final String aString) throws IllegalArgumentException {
        if (null == aString) {
            throw new IllegalArgumentException(CALENDAR_ERROR);
        }
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(convertEpochStringToCalendar(aString).getTime());
    }

    /**
     * Convert a {@code Calendar} instance to format &quot;dd-MM-yyyy&quot; .
     * @param aCalendar An instance of {@code Calendar}.
     * @return A {@code String} representation of {@code aCalendar} in format &quot;yyyy-MM-dd&quot;.
     * @throws IllegalArgumentException If {@code null == aCalendar}.
     */
    public static String getDateInDisplayFormat(final Calendar aCalendar) throws IllegalArgumentException {
        if (null == aCalendar) {
            throw new IllegalArgumentException(CALENDAR_ERROR);
        }
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(aCalendar.getTime());
    }

    /**
     * Get yesterday&#39;s date.
     * @param asUnixEpoch Pass {@code true} when you wish the date returned as a Unix Epoch seconds value.
     *                    Pass {@code false} to get the date in format &quot;yyyy-MM-dd&quot;.
     * @return A {@code String} representing yesterday in Unix Epoch seconds or in format &quot;yyyy-MM-dd&quot;.
     */
    public static String getYesterday(final boolean asUnixEpoch) {
        String out = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        if (asUnixEpoch) {
            out = getDateAsEpoch(cal);
        } else {
            out = getDateInSqlFormat(cal);
        }
        return out;
    }

    /**
     * Get yesterday&#39;s date.
     * @param asUnixEpoch Pass {@code true} when you wish the date returned as a Unix Epoch seconds value.
     *                    Pass {@code false} to get the date in format &quot;yyyy-MM-dd&quot;.
     * @return A {@code String} representing yesterday in Unix Epoch seconds or in format &quot;yyyy-MM-dd&quot;.
     */
    public static String getAWeekAgo(final boolean asUnixEpoch) {
        String out = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        if (asUnixEpoch) {
            out = getDateAsEpoch(cal);
        } else {
            out = getDateInSqlFormat(cal);
        }
        return out;
    }

    /**
     * Get the day equivalent to six months before today.
     * @param asUnixEpoch Pass {@code true} when you wish the date returned as a Unix Epoch seconds value.
     *                    Pass {@code false} to get the date in format &quot;yyyy-MM-dd&quot;.
     * @return A {@code String} representing the date in Unix Epoch seconds or in format &quot;yyyy-MM-dd&quot;.
     */
    public static String getSixMonthsAgo(final boolean asUnixEpoch) {
        String out = "";
        Calendar cal = Calendar.getInstance();
        // Withings advises to set dates with times as midday for sleep data.
        cal.add(Calendar.MONTH, -6);
        if (asUnixEpoch) {
            out = getDateAsEpoch(cal);
        } else {
            out = getDateInSqlFormat(cal);
        }
        return out;
    }

}
