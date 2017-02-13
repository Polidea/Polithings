package com.polidea.androidthings.driver.a4988.driver

enum class Resolution {
    FULL,
    HALF,
    QUARTER,
    EIGHT,
    SIXTEENTH;

    fun getStepMultiplier(): Int {
        when (this) {
            FULL -> return 16
            HALF -> return 8
            QUARTER -> return 4
            EIGHT -> return 2
            SIXTEENTH -> return 1
        }
    }
}