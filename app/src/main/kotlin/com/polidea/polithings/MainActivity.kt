package com.polidea.polithings

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.polidea.polithings.example.A4988StepperMotorActivity
import com.polidea.polithings.example.NumpadActivity
import com.polidea.polithings.example.ULN2003StepperMotorActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startNumpadExample()
//        startA4988Example()
//        startULN2003Example()
    }

    private fun startNumpadExample() {
        startActivity(Intent(this, NumpadActivity::class.java))
    }

    private fun startA4988Example() {
        startActivity(Intent(this, A4988StepperMotorActivity::class.java))
    }

    private fun startULN2003Example() {
        startActivity(Intent(this, ULN2003StepperMotorActivity::class.java))
    }
}
