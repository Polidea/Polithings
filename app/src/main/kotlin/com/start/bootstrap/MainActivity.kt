package com.start.bootstrap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import com.polidea.androidthings.driver.numpad.NumpadDriver



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         Log.d("MainActivity", "Hello world")

        NumpadDriver(c1GpioId = "BCM13",
                c2GpioId = "BCM5",
                c3GpioId = "BCM26",
                r1GpioId = "BCM6",
                r2GpioId = "BCM21",
                r3GpioId = "BCM20",
                r4GpioId = "BCM19"
        ).register()

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.d("onKeyDown", "${event.displayLabel}")
        return true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("onKeyUp", "${event?.displayLabel}")
        return true
    }
}
