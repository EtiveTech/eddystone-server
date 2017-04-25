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
 * Holder for the Withings distance data.
 * @author Craig
 */
public class Distance extends WithingsData {

    private static final long serialVersionUID = 3717963887300997445L;

    private Float distance;

    /**
     * Getter for the {@code distance} field.
     * @return The current value of {@code distance}, which will be
     *         an instance of {@code Float} or {@code null}.
     */
    public Float getDistance() {
        return distance;
    }

    /**
     * Setter for the {@code distance} field.
     * @param theDistance The instance of {@code Float} to set as the value.
     */
    public void setDistance(final Float theDistance) {
        this.distance = theDistance;
    }

}
