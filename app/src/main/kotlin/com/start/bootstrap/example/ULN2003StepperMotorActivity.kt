package com.start.bootstrap.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.polidea.androidthings.driver.steppermotor.Direction
import com.polidea.androidthings.driver.steppermotor.listener.RotationListener
import com.polidea.androidthings.driver.uln2003.driver.ULN2003Resolution
import com.polidea.androidthings.driver.uln2003.motor.ULN2003StepperMotor

class ULN2003StepperMotorActivity : AppCompatActivity() {

    val TAG = "ULN2003StepperMotor"

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

        stepper.rotate(degrees = 60.0,
                direction = Direction.CLOCKWISE,
                resolutionId = ULN2003Resolution.FULL.id,
                rpm = 1.0,
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
                resolutionId = ULN2003Resolution.HALF.id,
                rpm = 2.5)

        stepper.rotate(degrees = 180.0,
                direction = Direction.CLOCKWISE,
                resolutionId = ULN2003Resolution.FULL.id,
                rpm = 5.0)

        stepper.rotate(degrees = 180.0,
                direction = Direction.COUNTERCLOCKWISE,
                resolutionId = ULN2003Resolution.HALF.id,
                rpm = 8.0,
                rotationListener = object : RotationListener {
                    override fun onFinishedSuccessfully() {
                        Log.i(TAG, "all rotations finished")
                    }

                    override fun onFinishedWithError(degreesToRotate: Double, rotatedDegrees: Double, exception: Exception) {
                        Log.e(TAG, "error, degrees to rotate: {$degreesToRotate}  rotated degrees: {$rotatedDegrees}")
                    }
                })
    }
}
