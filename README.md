Android Things user-space drivers
=================================
Bunch of peripheral drivers for Android Things. If you need any other driver please let us know, we will do our best.

How to use the driver
-----------------------
Every driver is published as separeate lib to maven repository. 

Implemented drivers
-----------------------
Driver | Type | dependecy definition| usage  
:---:|:---:| --- | ---
[ULN2003](uln2003) | stepper motor| `compile 'com.polidea.androidthings.driver:uln2003:0.0.1-SNAPSHOT'` | [tutorial](https://gitlab2.polidea.com/pawel.byszewski/android_things_drivers/tree/master/uln2003) [sample](https://gitlab2.polidea.com/pawel.byszewski/android_things_drivers/blob/master/app/src/main/kotlin/com/start/bootstrap/example/StepperMotorActivity.kt)
[Numpad 12](numpad) | numpad with 12 buttons | `compile 'com.polidea.androidthings.driver:numpad12:0.0.1-SNAPSHOT'` | [tutorial](https://gitlab2.polidea.com/pawel.byszewski/android_things_drivers/tree/master/numpad) [sample](https://gitlab2.polidea.com/pawel.byszewski/android_things_drivers/blob/master/app/src/main/kotlin/com/start/bootstrap/MainActivity.kt) 
