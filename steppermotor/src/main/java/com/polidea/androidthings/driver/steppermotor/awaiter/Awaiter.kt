package com.polidea.androidthings.driver.steppermotor.awaiter

interface Awaiter {
    fun await(millis: Long, nanos: Int)
}