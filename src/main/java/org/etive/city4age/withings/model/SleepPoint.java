/*
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 * Copyright: Etive Technologies Ltd. 2016 (www.etive.org). All rights reserved.
 * Created  : 14-Dec-2016
 * Author   : Craig, (c dot speedie at etive dot org)
 * Enquiries: please send any enquiries to hello at etive dot org
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */
package org.etive.city4age.withings.model;

import java.util.Calendar;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 * Holder for the data contained in a Withings sleep data response series item.
 *
 * @author Craig
 */
public class SleepPoint implements Comparable<SleepPoint> {

    private Calendar start;
    private Calendar end;
    private long duration;
    /** Gap between start and end times in seconds represented as &quot;HH:mm&quot;. */
    private String formattedDuration;
    private String state;
    private String startDateTime;
    private String endDateTime;

    public SleepPoint(String theState, Calendar theStart, Calendar theEnd) {
        this.state = theState;
        this.start = theStart;
        this.end = theEnd;
        this.duration = 0L;
    }

    /**
     * Getter for the {@code duration} field.
     * @return The current value of {@code duration}, which will be
     *         a {@code long}.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Setter for the {@code duration} field.
     * @param theDuration The value of {@code duration}.
     */
    public void setDuration(final long theDuration) {
        this.duration = theDuration;
    }

    /**
     * Getter for the {@code formattedDuration} field.
     *
     * @return The current value of {@code formattedDuration}, which will be an {@code int}.
     */
    public String getFormattedDuration() {
        return formattedDuration;
    }

    /**
     * Setter for the {@code formattedDuration} field.
     *
     * @param theDuration The value to set.
     */
    public void setFormattedDuration(final String theDuration) {
        if (StringUtils.isBlank(theDuration)) {
            duration = end.getTimeInMillis() - start.getTimeInMillis();
            formattedDuration = DurationFormatUtils.formatDuration(duration, "HH:mm");
        } else {
            formattedDuration = theDuration;
        }
    }

    /**
     * Getter for the {@code state} field.
     *
     * @return The current value of {@code state}, which will be an instance of {@code String} or {@code null}.
     */
    public String getState() {
        return state;
    }

    /**
     * Setter for the {@code state} field.
     *
     * @param theState The instance of {@code Integer} to set as the value.
     */
    public void setState(final String theState) {
        state = theState;
    }

    /**
     * Getter for the {@code start} field.
     *
     * @return The current value of {@code start}, which will be an instance of {@code Calendar} or
     *         {@code null}.
     */
    public Calendar getStart() {
        return start;
    }

    /**
     * Setter for the {@code start} field.
     *
     * @param theStart The instance of {@code Calendar} to set as the value.
     */
    public void setStart(final Calendar theStart) {
        start = theStart;
    }

    /**
     * Getter for the {@code end} field.
     *
     * @return The current value of {@code end}, which will be an instance of {@code Calendar} or {@code null}.
     */
    public Calendar getEnd() {
        return end;
    }

    /**
     * Setter for the {@code end} field.
     *
     * @param theEnd The instance of {@code Calendar} to set as the value.
     */
    public void setEnd(final Calendar theEnd) {
        end = theEnd;
    }

    /**
     * Getter for the {@code endDateTime} field.
     * @return The current value of {@code endDateTime}, which will be
     *         an instance of {@code String} or {@code null}.
     */
    public String getEndDateTime() {
        return endDateTime;
    }

    /**
     * Setter for the {@code endDateTime} field.
     * @param theEndDateTime The instance of {@code String} to set as the value.
     */
    public void setEndDateTime(final String theEndDateTime) {
        if (StringUtils.isBlank(theEndDateTime)) {
            endDateTime = City4AgeDateUtils.getDateInDateTimeFormat(end);
        } else {
            this.endDateTime = theEndDateTime;
        }
    }

    /**
     * Getter for the {@code startDateTime} field.
     * @return The current value of {@code startDateTime}, which will be
     *         an instance of {@code String} or {@code null}.
     */
    public String getStartDateTime() {
        return startDateTime;
    }

    /**
     * Setter for the {@code startDateTime} field.
     * @param theStartDateTime The instance of {@code String} to set as the value.
     */
    public void setStartDateTime(final String theStartDateTime) {
        if (StringUtils.isBlank(theStartDateTime)) {
            startDateTime = City4AgeDateUtils.getDateInDateTimeFormat(start);
        } else {
            this.startDateTime = theStartDateTime;
        }
    }

    /**
     * Test to see if the gap between this and the {@code other} {@code SleepPoint} is greater
     * than five hours, which suggests the person is awake for the day.
     * @param other The instance of {@code SleepPoint} with which to compare.
     * @return {@code true} when the gap is greater than five hours; {@code false} otherwise.
     */
    public boolean indicatesNewSleepPeriod(SleepPoint other) {
        final int fiveHoursAsSeconds = 18000;
        return compareTo(other) > fiveHoursAsSeconds;
    }

    @Override
    public int compareTo(SleepPoint other) {
        int result = -1;
        if (null != other && null != other.getEnd()) {
            final Long diff = (this.getStart().getTimeInMillis() - other.getEnd().getTimeInMillis()) / 1000L;
            result = diff.intValue();
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\nSleepPoint{");
        sb.append("start: ");
        sb.append(City4AgeDateUtils.getDateInDateTimeFormat(start));
        sb.append(", end: ");
        sb.append(City4AgeDateUtils.getDateInDateTimeFormat(end));
        sb.append(", duration:");
        sb.append(formattedDuration);
        sb.append(", state: ");
        sb.append(state);
        sb.append("}");
        return sb.toString();
    }
}
