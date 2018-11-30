package com.polidea.polithings.example

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.polidea.androidthings.driver.a4988.driver.A4988Resolution
import com.polidea.androidthings.driver.a4988.motor.A4988StepperMotor
import com.polidea.androidthings.driver.steppermotor.Direction
import com.polidea.androidthings.driver.steppermotor.listener.RotationListener

class A4988StepperMotorActivity : AppCompatActivity() {

    private val TAG = "A4988StepperMotor"

    private val stepsPerRevolution = 96
    private val stepPin = "BCM20"
    private val dirPin = "BCM21"
    private val ms1Pin = "BCM5"
    private val ms2Pin = "BCM6"
    private val ms3Pin = "BCM19"

    private lateinit var stepper: A4988StepperMotor

    override fun onResume() {
        super.onResume()
        stepper = A4988StepperMotor(stepsPerRevolution, stepPin, dirPin, ms1Pin, ms2Pin, ms3Pin, null)
        stepper.rotate(degrees = 60.0,
                direction = Direction.CLOCKWISE,
                resolutionId = A4988Resolution.FULL.id,
                rpm = 10.0,
                rotationListener = object : RotationListener {
                    override fun onStarted() {
                        Log.i(TAG, "first rotation started")
                    }

                    override fun onFinishedSuccessfully() {
                        Log.i(TAG, "first rotation finished")
                    }

                    override fun onFinishedWithError(degreesToRotate: Double, rotatedDegrees: Double, exception: Exception) {
                        Log.e(TAG, "error, degrees to rotate: {$degreesToRotate}  rotated degrees: {$rotatedDegrees}")
                    }
                })

        stepper.rotate(degrees = 60.0,
                direction = Direction.COUNTERCLOCKWISE,
                resolutionId = A4988Resolution.HALF.id,
                rpm = 15.0)

        stepper.rotate(degrees = 180.0,
                direction = Direction.CLOCKWISE,
                resolutionId = A4988Resolution.QUARTER.id,
                rpm = 15.0)

        stepper.rotate(degrees = 180.0,
                direction = Direction.COUNTERCLOCKWISE,
                resolutionId = A4988Resolution.QUARTER.id,
                rpm = 25.0)

        stepper.rotate(degrees = 360.0,
                direction = Direction.CLOCKWISE,
                resolutionId = A4988Resolution.EIGHT.id,
                rpm = 35.0,
                rotationListener = object : RotationListener {
                    override fun onStarted() {
                        Log.i(TAG, "last rotation started")
                    }

                    override fun onFinishedSuccessfully() {
                        Log.i(TAG, "all rotations finished")
                    }

                    override fun onFinishedWithError(degreesToRotate: Double, rotatedDegrees: Double, exception: Exception) {
                        Log.e(TAG, "error, degrees to rotate: {$degreesToRotate}  rotated degrees: {$rotatedDegrees}")
                    }
                })
    }

    override fun onPause() {
        stepper.close()
        super.onPause()
    }
}
