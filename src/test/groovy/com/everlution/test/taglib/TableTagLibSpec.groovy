package com.everlution.test.taglib

import com.everlution.TableTagLib
import grails.testing.web.taglib.TagLibUnitTest
import org.grails.taglib.GrailsTagException
import spock.lang.Specification

class TableTagLibSpec extends Specification implements TagLibUnitTest<TableTagLib> {

    void "pagination throws error when total attribute not provided"() {
        when:
        tagLib.pagination()

        then:
        thrown(GrailsTagException)
    }

    void "continue here of course"() {
        expect:
        false
    }
}
