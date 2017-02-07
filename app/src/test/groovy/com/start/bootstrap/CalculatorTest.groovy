package com.start.bootstrap

import spock.lang.Specification


class CalculatorTest extends Specification {

    def "sample test success"() {
        given:
        def expectedValue = 2

        when:
        def result = new Calculator().calculate()

        then:
        result == expectedValue
    }
}
