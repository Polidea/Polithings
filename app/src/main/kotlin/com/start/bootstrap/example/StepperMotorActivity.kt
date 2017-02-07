package com.start.bootstrap.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.polidea.androidthings.driver.uln2003.Direction
import com.polidea.androidthings.driver.uln2003.motor.Interval
import com.polidea.androidthings.driver.uln2003.motor.MovementListener
import com.polidea.androidthings.driver.uln2003.motor.Resolution
import com.polidea.androidthings.driver.uln2003.motor.ULN2003StepperMotor

class StepperMotorActivity : AppCompatActivity() {

    val TAG = "StepperMotorActivity"

    val in1Pin = "BCM4"
    val in2Pin = "BCM17"
    val in3Pin = "BCM27"
    val in4Pin = "BCM22"

    private lateinit var stepper: ULN2003StepperMotor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        stepper = ULN2003StepperMotor(in1Pin, in2Pin, in3Pin, in4Pin)
        testStepper()
    }

    override fun onPause() {
        stepper.close()
        super.onPause()
    }

    private fun testStepper() {

        stepper.move(angle = 60.0,
                direction = Direction.CLOCKWISE,
                resolution = Resolution.FULL,
                movementListener = object : MovementListener {
                    override fun onStarted() {
                        Log.i(TAG, "first move started")
                    }

                    override fun onFinishedSuccessfully() {
                        Log.i(TAG, "first move finished")
                    }

                    override fun onFinishedWithError(angleToMove: Double, movedAngle: Double, exception: Exception) {
                        Log.e(TAG, "error, angle to move: {$angleToMove}  moved angle: {$movedAngle}")
                    }
                })

        stepper.move(angle = 60.0,
                direction = Direction.COUNTERCLOCKWISE,
                resolution = Resolution.HALF)

        stepper.move(angle = 180.0,
                direction = Direction.CLOCKWISE,
                resolution = Resolution.FULL,
                stepInterval = Interval(2, 500000))

        stepper.move(angle = 180.0,
                direction = Direction.COUNTERCLOCKWISE,
                resolution = Resolution.HALF,
                movementListener = object : MovementListener {
                    override fun onFinishedSuccessfully() {
                        Log.i(TAG, "all moves finished")
                    }

                    override fun onFinishedWithError(angleToMove: Double, movedAngle: Double, exception: Exception) {
                        Log.e(TAG, "error, angle to move: {$angleToMove}  moved angle: {$movedAngle}")
                    }
                })
    }
}
