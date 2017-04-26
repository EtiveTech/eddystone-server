package org.etive.city4age.withings.model

class WithingsActivity {

    private String date
    private Float calories
    private Float totalCalories
    private Float distance
    private Integer soft
    private Integer moderate
    private Integer intense
    private Integer steps

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
}
