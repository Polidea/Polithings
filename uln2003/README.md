ULN2003 driver for Android Things
================================

This driver supports ULN2003 driver with 28BYJ-48 Stepper Motor.<br/>
[This](http://42bots.com/tutorials/28byj-48-stepper-motor-with-uln2003-driver-and-arduino-uno/) tutorial helped us developing our driver.<br/>
<br/>
<img src="http://gitlab2.polidea.com/pawel.byszewski/android_things_drivers/raw/941597d1c24d220d158788d8ca5199781cd9d2f1/uln2003/readme/ULN2003.jpg" width="298" height="293" />

How to use the driver
---------------------

### Gradle dependency

To use the `uln2003` driver, simply add the line below to your project's `build.gradle`,
where `<version>` matches the last version of the driver available on [maven central](https://mvnrepository.com/search?q=polidea).

```
dependencies {
    compile 'com.polidea.androidthings.driver:uln2003:<version>'
}
```

### Hardware Setup
Connect all four "in" pins on uln2003 driver into GPIOs available on your board.

### Sample usage of high level Stepper Motor Driver

```kotlin
com.polidea.androidthings.driver.uln2003.motor.ULN2003StepperMotor

// select gpio pins on your board for uln2003 input pins

val in1Pin = "BCM4"
val in2Pin = "BCM17"
val in3Pin = "BCM27"
val in4Pin = "BCM22"

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

// select gpio pins on your board for uln2003 input pins

val in1Pin = "BCM4"
val in2Pin = "BCM17"
val in3Pin = "BCM27"
val in4Pin = "BCM22"

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

## License

    MIT License
    
    Copyright (c) 2017 Polidea Sp. z o.o
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.



## Maintained by

[![Polidea](https://raw.githubusercontent.com/Polidea/Polithings/master/readme/polidea_logo.png "Tailored software services including concept, design, development and testing")](http://www.polidea.com)
