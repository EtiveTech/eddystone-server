/*
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 * Copyright: Etive Technologies Ltd. 2016 (www.etive.org). All rights reserved.
 * Created  : 06-Dec-2016
 * Author   : Craig, (c dot speedie at etive dot org)
 * Enquiries: please send any enquiries to hello at etive dot org
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */
package org.etive.city4age.withings.model;

import java.io.Serializable;
import java.util.Calendar;
import org.apache.commons.lang.StringUtils;

/**
 * Base data for the Withings usage reports.
 * @author Craig
 */
public class WithingsData implements Comparable<WithingsData>, Serializable {

    private static final long serialVersionUID = 3143296449696938915L;

    private String date;

    /**
     * Getter for the {@code date} field.
     * @return The current value of {@code date}, which will be
     *         an instance of {@code String} or {@code null}.
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for the {@code date} field.
     * @param theDate The instance of {@code String} to set as the value.
     */
    public void setDate(final String theDate) {
        this.date = theDate;
    }

    @Override
    public int compareTo(WithingsData other) {
        int result = -1;
        String[] dateParts;
        if (null != other && StringUtils.isNotBlank(date) && StringUtils.isNotBlank(other.getDate())) {
            dateParts = this.date.split("-");
            Calendar cal1 = Calendar.getInstance();
            cal1.set(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]), 0, 0, 0);
            dateParts = other.date.split("-");
            Calendar cal2 = Calendar.getInstance();
            cal2.set(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]), 0, 0, 0);
            result = cal1.compareTo(cal2);
        }
        return result;
    }
}
