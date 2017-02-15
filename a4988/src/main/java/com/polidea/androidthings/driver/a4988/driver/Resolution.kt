package com.polidea.androidthings.driver.a4988.driver

enum class Resolution {
    FULL,
    HALF,
    QUARTER,
    EIGHT,
    SIXTEENTH;

    fun getStepMultiplier()
            = when (this) {
                FULL -> 16
                HALF -> 8
                QUARTER -> 4
                EIGHT -> 2
                SIXTEENTH -> 1
            }
}