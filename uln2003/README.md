ULN2003 driver for Android Things
================================

This driver supports ULN2003 driver with 28BYJ-48 Stepper Motor.<br/>
[This](http://42bots.com/tutorials/28byj-48-stepper-motor-with-uln2003-driver-and-arduino-uno/) tutorial helped us developing our driver.<br/>
<br/>
<img src="https://raw.githubusercontent.com/Polidea/Polithings/master/uln2003/readme/ULN2003.jpg" width="298" height="293" />

How to use the driver
---------------------

### Gradle dependency

To use the `uln2003` driver, simply add the line below to your project's `build.gradle`,
where `<version>` matches the last version of the driver available on [maven central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.polidea.androidthings.driver%22).

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
                                  
//Perform a rotation and add rotation listener
stepper.rotate(degrees = 180.0,
        direction = Direction.CLOCKWISE,
        resolutionId = ULN2003Resolution.HALF.id,
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
        
// Close the ULN2003StepperMotor when all moves are finished. Otherwise close() will terminate current and pending rotations.
stepper.close()
```

You can call `rotate` method multiple times. All tasks will be queued and invoked synchronously on a separate thread.<br/>
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

//Set resolution
uln2003.resolution = ULN2003Resolution.FULL

//Perform a step
uln2003.performStep(StepDuration(1))

// Close the ULN2003 when finished. 
uln2003.close()
```

Performing a half step gives you higher resolution and torque, but its maximum speed is lower than in a full step mode.<br/>

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
