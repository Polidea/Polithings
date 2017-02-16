package com.polidea.androidthings.driver.steppermotor.driver

import com.polidea.androidthings.driver.steppermotor.Direction

abstract class StepperMotorDriver : AutoCloseable {
    open var direction: Direction = Direction.CLOCKWISE

    abstract fun open()

    abstract fun performStep(stepDuration: StepDuration)
}