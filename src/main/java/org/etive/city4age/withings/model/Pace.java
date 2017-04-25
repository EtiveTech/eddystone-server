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
 * Holder for Withings activity intensity details.
 * @author Craig
 */
public class Pace extends WithingsData {

    private static final long serialVersionUID = 4311626405130937733L;

    /** Number of seconds of intense activity. */
    private Integer intense;
    /** Number of seconds of moderate activity. */
    private Integer moderate;
    /** Number of seconds of soft activity. */
    private Integer soft;

    /**
     * Getter for the {@code intense} field.
     * @return The current value of {@code intense}, which will be
     *         an instance of {@code Integer} or {@code null}.
     */
    public Integer getIntense() {
        return intense;
    }

    /**
     * Setter for the {@code intense} field.
     * @param theIntense The instance of {@code Integer} to set as the value.
     */
    public void setIntense(final Integer theIntense) {
        this.intense = theIntense;
    }

    /**
     * Getter for the {@code moderate} field.
     * @return The current value of {@code moderate}, which will be
     *         an instance of {@code Integer} or {@code null}.
     */
    public Integer getModerate() {
        return moderate;
    }

    /**
     * Setter for the {@code moderate} field.
     * @param theModerate The instance of {@code Integer} to set as the value.
     */
    public void setModerate(final Integer theModerate) {
        this.moderate = theModerate;
    }

    /**
     * Getter for the {@code soft} field.
     * @return The current value of {@code soft}, which will be
     *         an instance of {@code Integer} or {@code null}.
     */
    public Integer getSoft() {
        return soft;
    }

    /**
     * Setter for the {@code soft} field.
     * @param theSoft The instance of {@code Integer} to set as the value.
     */
    public void setSoft(final Integer theSoft) {
        this.soft = theSoft;
    }
}
