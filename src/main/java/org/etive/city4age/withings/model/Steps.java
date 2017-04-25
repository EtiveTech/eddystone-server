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
 * Holder for the Withings activity data relating to steps.
 * @author Craig
 */
public class Steps extends WithingsData {

    private static final long serialVersionUID = -7141804794370846979L;

    private Integer steps;

    /**
     * Getter for the {@code steps} field.
     * @return The current value of {@code steps}, which will be
     *         an instance of {@code Integer} or {@code null}.
     */
    public Integer getSteps() {
        return steps;
    }

    /**
     * Setter for the {@code steps} field.
     * @param theSteps The instance of {@code Integer} to set as the value.
     */
    public void setSteps(final Integer theSteps) {
        this.steps = theSteps;
    }
}
