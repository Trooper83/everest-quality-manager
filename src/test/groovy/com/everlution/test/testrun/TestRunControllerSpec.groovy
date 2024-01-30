package com.everlution.test.testrun

import com.everlution.TestRunController
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class TestRunControllerSpec extends Specification implements ControllerUnitTest<TestRunController> {

    def setup() {
    }

    def cleanup() {
    }

    void "returns bad request when project not found"() {
        // null and blank
        expect:"fix me"
            true == false
    }

    void "returns bad request when name is invalid"() {
        //null and blank
        expect:"fix me"
        true == false
    }

    void "... when no results provided"() {
        //null and blank
        expect:"fix me"
        true == false
    }

    void "... happy path"() {
        expect:"fix me"
        true == false
    }

    void "... bad request when test result invalid"() {
        expect:"fix me"
        true == false
    }

    void "... bad request when automated test invalid"() {
        expect:"fix me"
        true == false
    }

    void "returns test run id when successful"() {
        expect:"fix me"
        true == false
    }

    void "returns 201 when successful"() {
        expect:"fix me"
        true == false
    }
}
