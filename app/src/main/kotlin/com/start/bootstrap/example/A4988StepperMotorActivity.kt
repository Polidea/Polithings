package com.start.bootstrap.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.polidea.androidthings.driver.a4988.Direction
import com.polidea.androidthings.driver.a4988.driver.Resolution
import com.polidea.androidthings.driver.a4988.motor.A4988StepperMotor
import com.polidea.androidthings.driver.a4988.motor.RotationListener

class A4988StepperMotorActivity : AppCompatActivity() {

    val TAG = "A4988StepperMotor"

    val stepsPerRevolution = 96
    val stepPin = "BCM5"
    val dirPin = "BCM6"
    val ms1Pin = "BCM16"
    val ms2Pin = "BCM20"
    val ms3Pin = "BCM21"

    private lateinit var stepper: A4988StepperMotor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        stepper = A4988StepperMotor(stepsPerRevolution, stepPin, dirPin, ms1Pin, ms2Pin, ms3Pin, null)
        testStepper()
    }

    override fun onPause() {
        stepper.close()
        super.onPause()
    }

    private fun testStepper() {

        stepper.rotate(degrees = 60.0,
                direction = Direction.CLOCKWISE,
                resolution = Resolution.FULL,
                rpm = 10.0,
                rotationListener = object : RotationListener {
                    override fun onStarted() {
                        Log.i(TAG, "first move started")
                    }

                    override fun onFinishedSuccessfully() {
                        Log.i(TAG, "first move finished")
                    }

                    override fun onFinishedWithError(degreesToMove: Double, movedDegrees: Double, exception: Exception) {
                        Log.e(TAG, "error, degrees to move: {$degreesToMove}  moved degrees: {$movedDegrees}")
                    }
                })

        stepper.rotate(degrees = 60.0,
                direction = Direction.COUNTERCLOCKWISE,
                resolution = Resolution.HALF,
                rpm = 15.0)

        stepper.rotate(degrees = 180.0,
                direction = Direction.COUNTERCLOCKWISE,
                resolution = Resolution.QUARTER,
                rpm = 15.0)

        stepper.rotate(degrees = 180.0,
                direction = Direction.CLOCKWISE,
                resolution = Resolution.QUARTER,
                rpm = 25.0)

        stepper.rotate(degrees = 180.0,
                direction = Direction.COUNTERCLOCKWISE,
                resolution = Resolution.EIGHT,
                rpm = 35.0,
                rotationListener = object : RotationListener {
                    override fun onStarted() {
                        Log.i(TAG, "last move started")
                    }

                    override fun onFinishedSuccessfully() {
                        Log.i(TAG, "all moves finished")
                    }

                    override fun onFinishedWithError(degreesToMove: Double, movedDegrees: Double, exception: Exception) {
                        Log.e(TAG, "error, degrees to move: {$degreesToMove}  moved degrees: {$movedDegrees}")
                    }
                })
    }
}
