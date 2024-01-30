package com.everlution.test.testrun

import com.everlution.TestRunService
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class TestRunServiceSpec extends Specification implements ServiceUnitTest<TestRunService>{

    void "returns new with valid instance"() {
        expect:"fix me"
            true == false
    }

    void "throws validation exception with invalid instance"() {
        expect:"fix me"
        true == false
    }
}
