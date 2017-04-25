/*
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 * Copyright: Etive Technologies Ltd. 2016 (www.etive.org). All rights reserved.
 * Created  : 06-Dec-2016
 * Author   : Craig, (c dot speedie at etive dot org)
 * Enquiries: please send any enquiries to hello at etive dot org
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */
package org.etive.city4age.withings.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 * Holder for Withings sleep data. An instance of {@code Sleep} will be for one date with a {@code List} of
 * {@code Sleep.SleepPoints} describing the night's sleep.
 *
 * @author Craig
 */
public class Sleep extends WithingsData {

    private static final long serialVersionUID = 629154345946652184L;
    /**
     * A list of all the types of sleep for this instance e.g. awake, light, deep.
     */
    private final List<SleepPoint> sleepPoints;
    /**
     * A &quot;dd-MM-yyyy HH:mm&quot; representation of the {@code endDate} from the last added {@code SleepPoint}.
     */
    private String endDate;
    /**
     * The total sleep in seconds of all {@code sleepPoints} where {@code state.equals("awake")}.
     */
    private long sleepTotal;
    /**
     * * Representation of {@code sleepTotal} in format &quot;hh:mm&quot;.
     */
    private String formattedSleepTotal;

    public Sleep() {
        super();
        sleepPoints = new ArrayList<>();
        sleepTotal = 0L;
    }

    /**
     * Getter for the {@code endDate} field.
     *
     * @return The current value of {@code endDate}, which will be an instance of {@code String} or {@code null}.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Getter for the {@code sleepPoints} field.
     *
     * @return The current value of {@code sleepPoints}, which will be an instance of {@code List<SleepPoint>} or
     *         {@code null}.
     */
    public List<SleepPoint> getSleepPoints() {
        return sleepPoints;
    }

    /**
     * Setter for the {@code endDate} field.
     *
     * @param theEndDate The instance of {@code String} to set as the value.
     */
    public void setEndDate(final String theEndDate) {
        this.endDate = theEndDate;
    }

    /**
     * Setter for the {@code endDate} field using and instance of {@code SleepPoint}.
     *
     * @param point The instance of {@code SleepPoint} to set as the value.
     */
    public void setEndDate(final SleepPoint point) {
        if (point.getState().equals("awake")) {
            setEndDate(City4AgeDateUtils.getDateInDateTimeFormat(point.getStart()));
        } else {
            setEndDate(City4AgeDateUtils.getDateInDateTimeFormat(point.getEnd()));
        }
    }

    /**
     * Getter for the {@code sleepTotal} field.
     *
     * @return The current value of {@code sleepTotal}.
     */
    public long getSleepTotal() {
        return sleepTotal;
    }

    /**
     * Setter for the {@code sleepTotal} field.
     *
     * @param theTotalSleep The value to set.
     */
    public void setSleepTotal(final long theTotalSleep) {
        sleepTotal = theTotalSleep;
        formattedSleepTotal = DurationFormatUtils.formatDuration(sleepTotal, "HH:mm");
    }

    /**
     * Setter for the {@code sleepTotal} field that uses the duration values from all {@code SleepPoint}
     * instances within the {@code sleepPoints} field.
     */
    public void setSleepTotalFromSleepPoints() {
        sleepTotal = 0L;
        for (SleepPoint point : sleepPoints) {
            if (!point.getState().equals("awake")) {
                sleepTotal += point.getDuration();
            }
        }
        formattedSleepTotal = DurationFormatUtils.formatDuration(sleepTotal, "HH:mm");
    }

    /**
     * Getter for the {@code formattedSleepTotal} field.
     *
     * @return The current value of {@code formattedSleepTotal}, which will be an instance of {@code String} or
     *         {@code null}.
     */
    public String getFormattedSleepTotal() {
        return formattedSleepTotal;
    }

    /**
     * Setter for the {@code formattedSleepTotal} field.
     *
     * @param theFormattedTotalSleep The instance of {@code String} to set as the value.
     */
    public void setFormattedSleepTotal(final String theFormattedTotalSleep) {
        this.formattedSleepTotal = theFormattedTotalSleep;
    }

    /**
     * Add an instance of {@code SleepPoint} to the {@code sleepPoints} {@code ArrayList}.
     *
     * @param aPoint The instance of {@code SleepPoint} to add.
     */
    public void addSleepPoint(SleepPoint aPoint) {
        sleepPoints.add(aPoint);
    }

    /**
     * Get the number of items in {@code sleepPoints}.
     *
     * @return Zero or higher.
     */
    public int getPointCount() {
        return sleepPoints.size();
    }

    /**
     * Setter for the {@code date}field using and insatnce of {@code SleepPoint}.
     * @param point The {@code SleepPoint} instance to use.
     */
    public void setDate(final SleepPoint point) {
        if (point.getState().equals("awake")) {
            setDate(City4AgeDateUtils.getDateInDateTimeFormat(point.getEnd()));
        } else {
            setDate(City4AgeDateUtils.getDateInDateTimeFormat(point.getStart()));
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Sleep{");
        sb.append("\nstartDate : ");
        sb.append(super.getDate());
        sb.append("\nendDate   : ");
        sb.append(endDate);
        sb.append("\nsleepTotal: ");
        sb.append(sleepTotal);
        sb.append("\nformattedSleepTotal: ");
        sb.append(formattedSleepTotal);
        sb.append("\nsleepPoints:\n");
        sb.append(sleepPoints);
        return sb.toString();
    }


}
