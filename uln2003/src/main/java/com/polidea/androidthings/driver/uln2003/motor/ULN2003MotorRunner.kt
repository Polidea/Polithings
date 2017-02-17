package com.polidea.androidthings.driver.uln2003.motor

import com.polidea.androidthings.driver.steppermotor.Direction
import com.polidea.androidthings.driver.steppermotor.motor.MotorRunner
import com.polidea.androidthings.driver.uln2003.driver.ULN2003
import com.polidea.androidthings.driver.uln2003.driver.ULN2003Resolution

class ULN2003MotorRunner(val uln2003: ULN2003,
                         steps: Int,
                         direction: Direction,
                         val resolution: ULN2003Resolution,
                         executionDurationNanos: Long) : MotorRunner(uln2003, steps, direction, executionDurationNanos) {

    override fun applyResolution() {
        uln2003.resolution = resolution
    }
}