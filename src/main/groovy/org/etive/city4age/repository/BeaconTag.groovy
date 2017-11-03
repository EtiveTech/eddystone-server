package org.etive.city4age.repository

class BeaconTag {
    static private Long tag = 0;

    private Long mTagId

    BeaconTag() {
        if (tag == Long.MAX_VALUE) tag = 0
        mTagId = ++tag
    }

    def getId() {
        return mTagId
    }
}
