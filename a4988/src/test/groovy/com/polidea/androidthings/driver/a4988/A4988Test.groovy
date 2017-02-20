package com.polidea.androidthings.driver.a4988

import com.google.android.things.pio.Gpio
import com.polidea.androidthings.driver.a4988.driver.A4988
import com.polidea.androidthings.driver.a4988.driver.A4988Resolution
import com.polidea.androidthings.driver.steppermotor.Direction
import com.polidea.androidthings.driver.steppermotor.awaiter.Awaiter
import com.polidea.androidthings.driver.steppermotor.driver.StepDuration
import com.polidea.androidthings.driver.steppermotor.gpio.GpioFactory
import spock.lang.Specification
import spock.lang.Unroll

class A4988Test extends Specification {

    A4988 a4988
    GpioFactory gpioFactory = Mock GpioFactory
    Awaiter awaiter = Mock Awaiter
    def mockStep = Mock(Gpio)
    def mockDir = Mock(Gpio)
    def mockMS1 = Mock(Gpio)
    def mockMS2 = Mock(Gpio)
    def mockMS3 = Mock(Gpio)
    def mockEn = Mock(Gpio)

    void setup() {
        a4988 = new A4988("step", "dir", "ms1", "ms2", "ms3", "en", gpioFactory, awaiter)
        openA4988()
    }

    def "should close all gpios"() {
        when:
        a4988.close()

        then:
        1 * mockStep.close()
        1 * mockDir.close()
        1 * mockMS1.close()
        1 * mockMS2.close()
        1 * mockMS3.close()
        1 * mockEn.close()
    }

    @Unroll
    def "should set #direction direction"() {
        when:
        a4988.setDirection(direction)

        then:
        mockDir.setValue(dirValue)

        where:
        //@formatter:off
        direction                  || dirValue
        Direction.CLOCKWISE        || true
        Direction.COUNTERCLOCKWISE || false
        //@formatter:on
    }

    @Unroll
    def "should set #resolution resolution"() {
        when:
        a4988.setResolution(resolution)

        then:
        mockMS1.setValue(ms1Value)
        mockMS2.setValue(ms2Value)
        mockMS3.setValue(ms3Value)

        where:
        //@formatter:off
        resolution                 || ms1Value | ms2Value | ms3Value
        A4988Resolution.FULL       || true     | true     | true
        A4988Resolution.HALF       || true     | true     | false
        A4988Resolution.QUARTER    || false    | true     | false
        A4988Resolution.EIGHT      || true     | false    | false
        A4988Resolution.SIXTEENTH  || false    | false    | false
        //@formatter:on
    }

    def "should perform step"() {
        given:
        a4988.setEnabled(true)

        when:
        a4988.performStep(new StepDuration(0, 0))

        then:
        1 * mockStep.setValue(true)
        1 * mockStep.setValue(false)
    }

    def openA4988() {
        mockStep = Mock(Gpio)
        mockDir = Mock(Gpio)
        mockMS1 = Mock(Gpio)
        mockMS2 = Mock(Gpio)
        mockMS3 = Mock(Gpio)
        mockEn = Mock(Gpio)
        gpioFactory.openGpio("step") >> mockStep
        gpioFactory.openGpio("dir") >> mockDir
        gpioFactory.openGpio("ms1") >> mockMS1
        gpioFactory.openGpio("ms2") >> mockMS2
        gpioFactory.openGpio("ms3") >> mockMS3
        gpioFactory.openGpio("en") >> mockEn
        a4988.open()
    }
}