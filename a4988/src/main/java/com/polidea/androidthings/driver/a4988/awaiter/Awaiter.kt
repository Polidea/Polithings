package com.polidea.androidthings.driver.a4988.awaiter

interface Awaiter {
    fun await(millis: Long, nanos: Int)
}