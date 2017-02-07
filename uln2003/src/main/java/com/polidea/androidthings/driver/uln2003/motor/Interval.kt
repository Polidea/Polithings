package com.polidea.androidthings.driver.uln2003.motor

/**
 * Defines time interval between motor steps.
 * The smaller the time the faster a motor moves.
 * Default 1 millisecond seems to be optimal with no load
 */

data class Interval(val millis: Long = 1, val nanos: Int = 0) {
    init {
        if (millis < 0) {
            throw IllegalArgumentException("millis less than 0: {$millis}")
        }
        if (nanos < 0) {
            throw IllegalArgumentException("nanos less than 0: {$nanos}")
        }
    }
}