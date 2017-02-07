package com.polidea.androidthings.driver.uln2003

import com.google.android.things.pio.Gpio
import com.polidea.androidthings.driver.uln2003.driver.ULN2003
import com.polidea.androidthings.driver.uln2003.gpio.GpioFactory
import spock.lang.Specification
import spock.lang.Unroll

class ULN2003Test extends Specification {

    ULN2003 uln2003
    GpioFactory gpioFactory = Mock GpioFactory
    def mock1 = Mock(Gpio)
    def mock2 = Mock(Gpio)
    def mock3 = Mock(Gpio)
    def mock4 = Mock(Gpio)

    void setup() {
        uln2003 = new ULN2003("in1", "in2", "in3", "in4", gpioFactory)
        openULN2003()
    }

    def "should close all gpios"() {
        given:

        when:
        uln2003.close()

        then:
        1 * mock1.close()
        1 * mock2.close()
        1 * mock3.close()
        1 * mock4.close()
    }

    @Unroll
    def "should go from #stepState step to another in Half Step Mode clockwise"() {
        given:
        uln2003.setDirection(Direction.CLOCKWISE)
        uln2003.currentStepState = stepState

        when:
        uln2003.moveToNextHalfStep()

        then:
        mock1.setValue(io1Value)
        mock2.setValue(io2Value)
        mock3.setValue(io3Value)
        mock4.setValue(io4Value)

        where:
        //@formatter:off
        stepState             || io1Value | io2Value | io3Value | io4Value
        ULN2003.INITIAL_STATE || true     | false    | false    | false
        0                     || true     | true     | false    | false
        1                     || false    | true     | false    | false
        2                     || false    | true     | true     | false
        3                     || false    | false    | true     | false
        4                     || false    | false    | true     | true
        5                     || false    | false    | false    | true
        6                     || true     | false    | false    | true
        7                     || true     | true     | false    | false
        //@formatter:on
    }

    def openULN2003() {
        mock1 = Mock(Gpio)
        mock2 = Mock(Gpio)
        mock3 = Mock(Gpio)
        mock4 = Mock(Gpio)
        gpioFactory.openGpio("in1") >> mock1
        gpioFactory.openGpio("in2") >> mock2
        gpioFactory.openGpio("in3") >> mock3
        gpioFactory.openGpio("in4") >> mock4
        uln2003.open()
    }
}