Numpad driver for Android Things
================================

This driver supports numpad with 12 buttons.<br>
![numpad12](https://raw.githubusercontent.com/Polidea/Polithings/master/numpad/readme/numpad.jpg)

How to use the driver
---------------------

### Gradle dependency

To use the `numpad` driver, simply add the line below to your project's `build.gradle`,
where `<version>` matches the last version of the driver available on [maven central](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.polidea.androidthings.driver%22).

```
dependencies {
    compile 'com.polidea.androidthings.driver:numpad12:<version>'
}
```

### Hardware Setup
<img src="https://raw.githubusercontent.com/Polidea/Polithings/master/numpad/readme/numpad_wiring.jpg" width="342" height="389" /><br/>
Every `GPIO` connected to row on a numpad MUST be [pulled-up](https://developer.android.com/things/hardware/hardware-101.html#pull-ups_and_pull-downs) to 3.3V


### Sample usage

```kotlin
import com.polidea.androidthings.driver.numpad.NumpadDriver

// select gpio pins on your board for columns(c) and rows(r)

val c1GpioPinName = "BCM27"
val c2GpioPinName = "BCM17"
val c3GpioPinName = "BCM22"
val r1GpioPinName = "BCM12"
val r2GpioPinName = "BCM21"
val r3GpioPinName = "BCM20"
val r4GpioPinName = "BCM16"

// Access the Button and listen for events:

val numpad = Numpad(c1GpioId = c1GpioPinName,
                    c2GpioId = c2GpioPinName,
                    c3GpioId = c3GpioPinName,
                    r1GpioId = r1GpioPinName,
                    r2GpioId = r2GpioPinName,
                    r3GpioId = r3GpioPinName,
                    r4GpioId = r4GpioPinName
        )
        .apply {
            keyListener = { keyEvent ->
                Log.d("keypad event", "${keyEvent.displayLabel}")
            }
        }
numpad.register()
// Close the Numpad when finished:

numpad.close()
```

Alternatively, you can register a `NumpadDriver` with the system and receive `KeyEvent`s
through the standard Android APIs:
```kotlin
val numpadInputDriver = NumpadDriver(c1GpioId = c1GpioPinName,
                                    c2GpioId = c2GpioPinName,
                                    c3GpioId = c3GpioPinName,
                                    r1GpioId = r1GpioPinName,
                                    r2GpioId = r2GpioPinName,
                                    r3GpioId = r3GpioPinName,
                                    r4GpioId = r4GpioPinName
)
numpadInputDriver.register()

// Override key event callbacks in your Activity:

override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
    Log.d("onKeyDown", "${event.displayLabel}")
    return true
}

override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
    Log.d("onKeyUp", "${event?.displayLabel}")
    return true
}

// Unregister and close the input driver when finished:

numpadInputDriver.unregister()
```

The `keycode` could be one of:
```
KeyEvent.KEYCODE_1
KeyEvent.KEYCODE_2
KeyEvent.KEYCODE_3
KeyEvent.KEYCODE_4
KeyEvent.KEYCODE_5
KeyEvent.KEYCODE_6
KeyEvent.KEYCODE_7
KeyEvent.KEYCODE_8
KeyEvent.KEYCODE_9
KeyEvent.KEYCODE_0
KeyEvent.KEYCODE_STAR
KeyEvent.KEYCODE_POUND
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
