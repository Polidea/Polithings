A4988 driver for Android Things
================================

This driver supports A4988 Stepper Motor Driver.<br/>
<br/>
<img src="https://raw.githubusercontent.com/Polidea/Polithings/master/a4988/readme/A4988.jpg" width="300" height="300" />

How to use the driver
---------------------

### Gradle dependency

To use the `A4988` driver, simply add the line below to your project's `build.gradle`,
where `<version>` matches the last version of the driver available on [maven central](https://mvnrepository.com/search?q=polidea).

```
dependencies {
    compile 'com.polidea.androidthings.driver:a4988:<version>'
}
```

### Hardware Setup

<img src="https://raw.githubusercontent.com/Polidea/Polithings/master/a4988/readme/a4988_wiring.jpg" width="580" height="410" />

### Sample usage of high level Stepper Motor Driver

```kotlin
com.polidea.androidthings.driver.a4988.motor.A4988StepperMotor

// select gpio pins on your board for A4988 input pins
 
val stepPin = "BCM20"
val dirPin = "BCM21"
val ms1Pin = "BCM5"
val ms2Pin = "BCM6"
val ms3Pin = "BCM19"

// set steps per revolution according to type of your stepper motor
val stepsPerRevolution = 96

val stepper = A4988StepperMotor(stepsPerRevolution, stepPin, dirPin, ms1Pin, ms2Pin, ms3Pin, null)
                                  
//Perform a rotation and add rotation listener
stepper.rotate(degrees = 180.0,
        direction = Direction.CLOCKWISE,
        resolutionId = A4988Resolution.HALF.id,
        rpm = 2.5,
        rotationListener = object : RotationListener {
            override fun onStarted() {
                Log.i(TAG, "rotation started")
            }
            override fun onFinishedSuccessfully() {
                Log.i(TAG, "rotation finished")
            }
            override fun onFinishedWithError(degreesToRotate: Double, rotatedDegrees: Double, exception: Exception) {
                Log.e(TAG, "error, degrees to rotate: {$degreesToRotate}  rotated degrees: {$rotatedDegrees}")
            }
        })
        
// Close the A4988StepperMotor when all moves are finished. Otherwise close() will terminate current and pending rotations.
stepper.close()
```

You can call `rotate` method multiple times. All tasks will be queued and invoked synchronously on a separate thread.<br/>
Remember, if you close A4988StepperMotor during the execution process you'll be notified about the error from the running task only.

### Sample usage of A4988

```kotlin
package com.polidea.androidthings.driver.a4988.driver.A4988

// select gpio pins on your board for A4988 input pins

        val stepPin = "BCM20"
        val dirPin = "BCM21"
        val ms1Pin = "BCM5"
        val ms2Pin = "BCM6"
        val ms3Pin = "BCM19"

        val a4988 = A4988(stepGpioId = stepPin,
                dirGpioId = dirPin,
                ms1GpioId = ms1Pin,
                ms2GpioId = ms2Pin,
                ms3GpioId = ms3Pin,
                enGpioId = null)

// Open A4988 gpios
        a4988.open()

//Set direction
        a4988.direction = Direction.COUNTERCLOCKWISE

//Set resolution
        a4988.resolution = A4988Resolution.FULL

//Perform a step
        a4988.performStep(StepDuration(10))

// Close the A4988 when finished.
        a4988.close()
```

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
