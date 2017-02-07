package com.polidea.androidthings.driver.numpad

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import com.polidea.androidthings.driver.numpad.domain.Column
import com.polidea.androidthings.driver.numpad.domain.ColumnFactory
import com.polidea.androidthings.driver.numpad.domain.Row
import com.polidea.androidthings.driver.numpad.domain.RowFactory
import java.util.concurrent.atomic.AtomicBoolean



class Numpad internal constructor(private val c1GpioId: String,
             private val c2GpioId: String,
             private val c3GpioId: String,
             private val r1GpioId: String,
             private val r2GpioId: String,
             private val r3GpioId: String,
             private val r4GpioId: String,
             private val columnFactory: ColumnFactory,
             private val rowFactory: RowFactory) {

    constructor(c1GpioId: String,
                 c2GpioId: String,
                 c3GpioId: String,
                 r1GpioId: String,
                 r2GpioId: String,
                 r3GpioId: String,
                 r4GpioId: String) :
            this(c1GpioId, c2GpioId, c3GpioId,
                 r1GpioId, r2GpioId, r3GpioId, r4GpioId,
                 ColumnFactory(), RowFactory())


    private lateinit var columns: List<Column>
    private lateinit var rows: List<Row>
    private var lastKey: Key? = null
    private val isActive: AtomicBoolean = AtomicBoolean(false)

    var keyListener: ((KeyEvent) -> Unit)? = null

    fun register() {
        activate()
        registerColumns()
        registerRows()
        runOnNewThread {
            observeNumpad()
        }
    }

    private fun observeNumpad() {
        while (isActive.get()) {
            val pressedKey = checkPressedKey()

            if (lastKey == null && pressedKey != null) {
                onNewKeyDetected(pressedKey)
            }
            if (pressedKey == null) {
                resetLastDetectedKey()
            }
        }
    }

    private fun resetLastDetectedKey() {
        lastKey?.postKeyUp()
        lastKey = null
    }

    private fun onNewKeyDetected(pressedKey: Pair<Column, Row>) {
        val key = decodeKey(pressedKey.first, pressedKey.second)
        lastKey = key
        key.postKeyDown()
    }

    private fun runOnNewThread(action: () -> Unit) {
        Thread { action() }.start()
    }

    private fun activate() {
        isActive.set(true)
    }

    private fun checkPressedKey(): Pair<Column, Row>? {
        columns.forEach { column ->
            column.state = true
            Thread.sleep(10)
            rows.find(Row::isActive)?.let { return Pair(column, it) }
            column.state = false
        }
        return null
    }

    private fun registerRows() {
        rows = listOf(
                rowFactory.create(r1GpioId, Row.Id.R1),
                rowFactory.create(r2GpioId, Row.Id.R2),
                rowFactory.create(r3GpioId, Row.Id.R3),
                rowFactory.create(r4GpioId, Row.Id.R4)
        )
    }

    private fun registerColumns() {
        columns = listOf(
                columnFactory.create(c1GpioId, Column.Id.C1),
                columnFactory.create(c2GpioId, Column.Id.C2),
                columnFactory.create(c3GpioId, Column.Id.C3)
        )
    }

    fun unregister() {
        isActive.set(false)
        rows.forEach(Row::close)
        columns.forEach(Column::close)
    }

    private fun Numpad.Key.postKeyDown() {
        Log.d("Numpad", "post down ${this.name}")
        postKeyAction(KeyEvent.ACTION_DOWN)
    }

    private fun Numpad.Key.postKeyUp() {
        Log.d("Numpad", "post up ${this.name}")
        postKeyAction(KeyEvent.ACTION_UP)
    }

    private fun Numpad.Key.postKeyAction(keyAction: Int) {
        keyListener?.let {
            runOnUiThread {
                val keyEvent = KeyEvent(keyAction, keyCode)
                Log.d("Numpad", "Emit ${keyEvent.displayLabel} from Numpad with action ${keyAction}")
                it.invoke(keyEvent)
            }
        }
    }

    private fun decodeKey(column: Column, row: Row): Key
         = when (column.id) {
            Column.Id.C1 -> when (row.id) {
                Row.Id.R1 -> Key.ONE
                Row.Id.R2 -> Key.FOUR
                Row.Id.R3 -> Key.SEVEN
                Row.Id.R4 -> Key.STAR
            }
            Column.Id.C2 -> when (row.id) {
                Row.Id.R1 -> Key.TWO
                Row.Id.R2 -> Key.FIVE
                Row.Id.R3 -> Key.EIGHT
                Row.Id.R4 -> Key.ZERO
            }
            Column.Id.C3 -> when (row.id) {
                Row.Id.R1 -> Key.THREE
                Row.Id.R2 -> Key.SIX
                Row.Id.R3 -> Key.NINE
                Row.Id.R4 -> Key.HASH
            }
        }

    private fun runOnUiThread(action: () -> Unit) {
        Handler(Looper.getMainLooper())
            .post { action() }
    }

    enum class Key(val keyCode: Int) {
        ONE(KeyEvent.KEYCODE_1),
        TWO(KeyEvent.KEYCODE_2),
        THREE(KeyEvent.KEYCODE_3),
        FOUR(KeyEvent.KEYCODE_4),
        FIVE(KeyEvent.KEYCODE_5),
        SIX(KeyEvent.KEYCODE_6),
        SEVEN(KeyEvent.KEYCODE_7),
        EIGHT(KeyEvent.KEYCODE_8),
        NINE(KeyEvent.KEYCODE_9),
        ZERO(KeyEvent.KEYCODE_0),
        STAR(KeyEvent.KEYCODE_STAR),
        HASH(KeyEvent.KEYCODE_POUND)
    }
}

