package com.polidea.androidthings.driver.a4988.driver

data class PulseDuration(val millis: Long = 1, val nanos: Int = 0) {
    init {
        if (millis < 0) {
            throw IllegalArgumentException("millis less than 0: {$millis}")
        }
        if (nanos < 0) {
            throw IllegalArgumentException("nanos less than 0: {$nanos}")
        }
    }
}