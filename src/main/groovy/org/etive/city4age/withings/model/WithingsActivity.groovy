package org.etive.city4age.withings.model

import org.etive.city4age.repository.CareReceiver

class WithingsActivity {

    private String date
    private Float calories
    private Float totalCalories
    private Float distance
    private Integer soft
    private Integer moderate
    private Integer intense
    private Integer steps
    private CareReceiver careReceiver

    def setDate(String date) {
        this.date = date
    }

    String getDate() {
        return this.date
    }

    def setCalories(Float calories) {
        this.calories = calories
    }

    def setTotalCalories(Float totalCalories) {
        this.totalCalories = totalCalories
    }

    def setDistance(Float distance) {
        this.distance = distance
    }

    def setSoft(Integer soft) {
        this.soft = soft
    }

    def setModerate(Integer moderate) {
        this.moderate = moderate
    }

    def setIntense(Integer intense) {
        this.intense = intense
    }

    def setSteps(Integer steps) {
        this.steps = steps
    }

    def setCareReceiver(CareReceiver careReceiver) {
        this.careReceiver = careReceiver
    }

    def toMap() {
        return [
                date: this.date,
                calories: this.calories,
                totalCalories: this.totalCalories,
                distance: this.distance,
                soft: this.soft,
                moderate: this.moderate,
                intense: this.intense,
                steps: this.steps,
                careReceiver: this.careReceiver
        ]
    }
}
