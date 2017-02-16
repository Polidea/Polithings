package com.polidea.androidthings.driver.a4988.motor

import com.polidea.androidthings.driver.a4988.driver.A4988
import com.polidea.androidthings.driver.a4988.driver.A4988Resolution
import com.polidea.androidthings.driver.steppermotor.Direction
import com.polidea.androidthings.driver.steppermotor.motor.MotorRunner

class A4988MotorRunner(val a4988: A4988,
                       steps: Int,
                       direction: Direction,
                       val resolution: A4988Resolution,
                       executionDurationNanos: Long) : MotorRunner(a4988, steps, direction, executionDurationNanos) {

    override fun applyResolution() {
        a4988.resolution = resolution
    }
}