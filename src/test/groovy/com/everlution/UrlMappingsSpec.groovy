package com.everlution

import grails.testing.web.UrlMappingsUnitTest
import spock.lang.Specification

class UrlMappingsSpec extends Specification implements UrlMappingsUnitTest<UrlMappings> {

    void setup() {
        mockController(TestCaseController)
    }

    void "test forward and reverse mappings"() {
        expect:
        verifyUrlMapping("/", view: 'index')
        verifyUrlMapping("/testCase/index", controller: 'testCase', action: 'index')
        verifyUrlMapping("/testCase/create", controller: 'testCase', action: 'create')
        verifyUrlMapping("/testCase/save", controller: 'testCase', action: 'save')
        verifyUrlMapping("/testCase/update", controller: 'testCase', action: 'update')
        verifyUrlMapping("/testCase/show/123", controller: 'testCase', action: 'show') {
            id = 123
        }
        verifyUrlMapping("/testCase/edit/123", controller: 'testCase', action: 'edit') {
            id = 123
        }
        verifyUrlMapping("/testCase/delete/123", controller: 'testCase', action: 'delete') {
            id = 123
        }
    }

    void "test forward error mappings"() {
        expect:
        verifyForwardUrlMapping(500, view: 'error')
        verifyForwardUrlMapping(404, view: 'notFound')
    }

}
