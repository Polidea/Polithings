ULN2003 driver for Android Things
================================

This driver supports ULN2003 driver with 28BYJ-48 Stepper Motor.<br/>
[This](http://42bots.com/tutorials/28byj-48-stepper-motor-with-uln2003-driver-and-arduino-uno/) tutorial helped us developing our driver.

How to use the driver
---------------------

### Gradle dependency

To use the `uln2003` driver, simply add the line below to your project's `build.gradle`,
where `<version>` matches the last version of the driver available on TBD.

```
dependencies {
    TBD
}
```

### Connection
Connect all four "in" pins on uln2003 driver into GPIOs available on your board

### Sample usage of high level Stepper Motor Driver

```kotlin
com.polidea.androidthings.driver.uln2003.motor.ULN2003StepperMotor

val stepper = ULN2003StepperMotor(in1GpioId = in1Pin,
                                  in2GpioId = in2Pin,
                                  in3GpioId = in3Pin,
                                  in4GpioId = in4Pin)
                                  
//Perform a move and add movement listener

stepper.move(angle = 180.0, direction = Direction.CLOCKWISE, resolution = Resolution.HALF, movementListener = object : OnMoveFinishedListener {
            override fun onStarted() {
                Log.i(TAG, "the move has started")
            }
            override fun onFinishedSuccessfully() {
                Log.i(TAG, "the move has finished successfully")
            }
            override fun onFinishedWithError(angleToMove: Double, movedAngle: Double, exception: Exception) {
                Log.e(TAG, "the move has finished with an error, angle to move: {$angleToMove}  moved angle: {$movedAngle}")
            }
        },
        stepInterval = Interval(1, 500000))
        
// Close the ULN2003StepperMotor when all moves are finished. Otherwise close() will terminate current and pending moves. 

stepper.close()
```

You can call `move` method multiple times. All tasks will be queued and invoked synchronously on a separate thread.<br/>
Remember, if you close ULN2003StepperMotor during the execution process you'll be notified about the error from the running task only.

### Sample usage of ULN2003

```kotlin
package com.polidea.androidthings.driver.uln2003.driver.ULN2003

val uln2003 = ULN2003(in1GpioId = in1Pin,
                      in2GpioId = in2Pin,
                      in3GpioId = in3Pin,
                      in4GpioId = in4Pin)
                      
// Open uln2003 gpios

uln2003.open()

//Set direction

uln2003.direction = Direction.COUNTERCLOCKWISE

//Perform full or half step

uln2003.moveToNextHalfStep()
Thread.sleep(1)
uln2003.moveToNextHalfStep()

// Close the ULN2003 when finished. 

uln2003.close()
```

Performing a half step gives you higher resolution and torque but it's slower than a full step.<br/>
To cover a full circle you should call `moveToNextHalfStep` method 4076 times according to tutorial mentioned above 