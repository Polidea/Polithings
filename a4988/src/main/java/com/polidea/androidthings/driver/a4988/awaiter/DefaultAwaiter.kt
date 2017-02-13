package com.polidea.androidthings.driver.a4988.awaiter

class DefaultAwaiter(val busyAwaitThresholdMillis: Long = 2, val busyAwaitThresholdNanos: Int = 0) : Awaiter {

    override fun await(millis: Long, nanos: Int) {
        if (millis < busyAwaitThresholdMillis ||
                millis >= busyAwaitThresholdMillis && nanos < busyAwaitThresholdNanos) {
            busyAwait(millis, nanos)
        } else {
            sleep(millis, nanos)
        }
    }

    private fun sleep(millis: Long, nanos: Int)
            = Thread.sleep(millis, nanos)

    private fun busyAwait(millis: Long, nanos: Int) {
        val awaitFinishNanos = System.nanoTime() + getNanos(millis, nanos)
        while (System.nanoTime() < awaitFinishNanos) {
        }
    }

    private fun getNanos(millis: Long, nanos: Int): Long
            = millis * NANOS_IN_MILLISECOND + nanos.toLong()

    companion object {
        val NANOS_IN_MILLISECOND = 1000000L
    }
}