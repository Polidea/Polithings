package com.polidea.androidthings.driver.numpad.domain

import com.polidea.androidthings.driver.numpad.hardware.NumpadKey


internal open class Row(gpioId: String,
                   open val id: Id,
                   numpadKeyFactory: NumpadKeyFactory) : AutoCloseable  {

    val rowState: NumpadKey = numpadKeyFactory.create(gpioId, NumpadKey.LogicState.PRESSED_WHEN_LOW)

    open val isActive: Boolean
        get() = rowState.state

    override fun close() {
        rowState.close()
    }

    enum class Id {  R1, R2, R3, R4 }
}

internal open class RowFactory(val numpadKeyFactory: NumpadKeyFactory = NumpadKeyFactory()) {

    open fun create(gpioId: String, id: Row.Id)
            = Row(gpioId, id, numpadKeyFactory)
}

class NumpadKeyFactory {
    fun create(pin: String, logicLevel: NumpadKey.LogicState)
            = NumpadKey(pin, logicLevel)
}