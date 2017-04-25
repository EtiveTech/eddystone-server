/*
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 * Copyright: Etive Technologies Ltd. 2016 (www.etive.org). All rights reserved.
 * Created  : 06-Dec-2016
 * Author   : Craig, (c dot speedie at etive dot org)
 * Enquiries: please send any enquiries to hello at etive dot org
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */
package org.etive.city4age.withings.model;

/**
 * Holder for the Withings activity data relating to calories burned.
 * @author Craig
 */
public class Calories extends WithingsData {

    private static final long serialVersionUID = 2111063322498966945L;

    private Double calories;
    private Double totalCalories;

    /**
     * Getter for the {@code calories} field.
     * @return The current value of {@code calories}, which will be
     *         an instance of {@code Double} or {@code null}.
     */
    public Double getCalories() {
        return calories;
    }

    /**
     * Setter for the {@code calories} field.
     * @param theCalories The instance of {@code Double} to set as the value.
     */
    public void setCalories(final Double theCalories) {
        this.calories = theCalories;
    }

    /**
     * Getter for the {@code totalCalories} field.
     * @return The current value of {@code totalCalories}, which will be
     *         an instance of {@code Double} or {@code null}.
     */
    public Double getTotalCalories() {
        return totalCalories;
    }

    /**
     * Setter for the {@code totalCalories} field.
     * @param theTotalCalories The instance of {@code Double} to set as the value.
     */
    public void setTotalCalories(final Double theTotalCalories) {
        this.totalCalories = theTotalCalories;
    }
}
