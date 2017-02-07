package com.polidea.androidthings.driver.numpad

import com.polidea.androidthings.driver.numpad.domain.Column
import com.polidea.androidthings.driver.numpad.domain.ColumnFactory
import com.polidea.androidthings.driver.numpad.domain.Row
import com.polidea.androidthings.driver.numpad.domain.RowFactory
import spock.lang.Specification
import spock.lang.Unroll

class NumpadTest extends Specification {

    Numpad numpad
    ColumnFactory columnFactory = Mock ColumnFactory
    RowFactory rowFactory = Mock RowFactory
    Row row = Mock Row
    Column column = Mock Column

    void setup() {
        numpad = new Numpad("c1", "c2", "c3",
                            "r1", "r2", "r3", "r4",
                             columnFactory, rowFactory)
        columnFactory.create(_, _) >> column
        rowFactory.create(_, _) >> row
    }

    def "should not detect key if any is pressed"() {
        given:
        def invoked = false
        numpad.setKeyListener { invoked = true }
        numpad.activate()
        numpad.registerColumns()
        numpad.registerRows()

        when:
        numpad.checkPressedKey()

        then:
        invoked == false
    }

    def "should activate all columns"() {
        given:
        numpad.registerColumns()
        numpad.registerRows()

        when:
        numpad.checkPressedKey()

        then:
        3 * column.setState(true)
    }

    def "should check all rows for all columns"() {
        given:
        numpad.registerColumns()
        numpad.registerRows()

        when:
        numpad.checkPressedKey()

        then:
        (3 * 4) * row.isActive()
    }

    def "should close all pins"() {
        given:
        numpad.registerColumns()
        numpad.registerRows()

        when:
        numpad.unregister()

        then:
        4 * row.close()
        3 * column.close()
    }

    @Unroll
    def "should decode key from #columnId and #rowId to #expectedKey"() {
        given:
        def column = Mock Column
        column.getId() >> columnId

        def row = Mock Row
        row.getId() >> rowId

        when:
        def key = numpad.decodeKey(column, row)

        then:
        key == expectedKey

        where:
        columnId        |   rowId       ||  expectedKey
        Column.Id.C1    |   Row.Id.R1   ||  Numpad.Key.ONE
        Column.Id.C1    |   Row.Id.R2   ||  Numpad.Key.FOUR
        Column.Id.C1    |   Row.Id.R3   ||  Numpad.Key.SEVEN
        Column.Id.C1    |   Row.Id.R4   ||  Numpad.Key.STAR

        Column.Id.C2    |   Row.Id.R1   ||  Numpad.Key.TWO
        Column.Id.C2    |   Row.Id.R2   ||  Numpad.Key.FIVE
        Column.Id.C2    |   Row.Id.R3   ||  Numpad.Key.EIGHT
        Column.Id.C2    |   Row.Id.R4   ||  Numpad.Key.ZERO

        Column.Id.C3    |   Row.Id.R1   ||  Numpad.Key.THREE
        Column.Id.C3    |   Row.Id.R2   ||  Numpad.Key.SIX
        Column.Id.C3    |   Row.Id.R3   ||  Numpad.Key.NINE
        Column.Id.C3    |   Row.Id.R4   ||  Numpad.Key.HASH
    }
}
