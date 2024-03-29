package com.everlution.test.unit.taglib

import com.everlution.taglibs.UtilityTagLib
import grails.testing.web.taglib.TagLibUnitTest
import org.grails.taglib.GrailsTagException
import spock.lang.Specification

import java.text.DecimalFormat

class UtilityTagLibSpec extends Specification implements TagLibUnitTest<UtilityTagLib> {

    void "multiProgressBar tag throws error when total not provided"() {
        when:
        tagLib.multiProgressBar()

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [multiProgressBar] is missing required attribute [total]"
    }

    void "multiProgressBar tag throws error when todo not provided"() {
        when:
        tagLib.multiProgressBar(total:0)

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [multiProgressBar] is missing required attribute [todo]"
    }

    void "multiProgressBar tag throws error when passed not provided"() {
        when:
        tagLib.multiProgressBar(total:0, todo:0)

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [multiProgressBar] is missing required attribute [passed]"
    }

    void "multiProgressBar tag throws error when failed not provided"() {
        when:
        tagLib.multiProgressBar(total:0, todo:0, passed:0)

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [multiProgressBar] is missing required attribute [failed]"
    }

    void "tag outputs empty string if values are greater than total"() {
        when:
        def found =
                applyTemplate("<g:multiProgressBar total='2' todo='5' passed='5' failed='5}'/>")

        then:
        found == ''
    }

    void "tag outputs correct numbers when value is null"() {
        when:
        def found =
                applyTemplate("<g:multiProgressBar total='2' todo='null' passed='0' failed='2'/>")

        then:
        found == createHtml(0, 0.00, 2, 100.00, 0, 0.00)
    }

    void "tag outputs empty string when total is null"() {
        when:
        def found =
                applyTemplate("<g:multiProgressBar total='null' todo='0' passed='0' failed='2'/>")

        then:
        found == ''
    }

    void "tag outputs empty string when value count is less than total"() {
        when:
        def found =
                applyTemplate("<g:multiProgressBar total='10' todo='0' passed='0' failed='2'/>")

        then:
        found == ''
    }

    void "tag outputs correct numbers"(int total, int passed, double passPercent, int failed, double failPercent,
                                       int todo, double todoPercent) {
        when:
        def found =
                applyTemplate("<g:multiProgressBar total='${total}' todo='${todo}' passed='${passed}' failed='${failed}'/>")

        then:
        found == createHtml(passed, passPercent, failed, failPercent, todo, todoPercent)

        where:
        total | passed | passPercent | failed | failPercent | todo | todoPercent
        0     | 0      | 0.00        | 0      | 0.00        | 0    | 0.00
        10    | 2      | 20.00       | 6      | 60.00       | 2    | 20.00
        10    | 0      | 0           | 0      | 0           | 10   | 100
        9     | 3      | 33.33       | 3      | 33.33       | 3    | 33.33
    }

    private String createHtml(int passed, double passPercent, int failed, double failPercent,
                              int todoCount, double todoPercent) {
        def sb = new StringBuilder()

        final DecimalFormat df = new DecimalFormat("0.00");

        def start = "<div class='progress-stacked w-50'>"
        def pass = "<div class='progress' role='progressbar' aria-label='passed' aria-valuenow='${passed}'"
        def pass_1 = " aria-valuemin='0' aria-valuemax='100' style='width: ${df.format(passPercent)}%'><div class='progress-bar bg-success'></div></div>"
        def fail = "<div class='progress' role='progressbar' aria-label='failed' aria-valuenow='${failed}'"
        def fail_1 =
                " aria-valuemin='0' aria-valuemax='100' style='width: ${df.format(failPercent)}%'><div class='progress-bar bg-danger'></div></div>"
        def todo = "<div class='progress' role='progressbar' aria-label='todo' aria-valuenow='${todoCount}'"
        def todo_1 =
                " aria-valuemin='0' aria-valuemax='100' style='width: ${df.format(todoPercent)}%'><div class='progress-bar bg-secondary'></div></div>"
        def end = '</div>'
        sb.append(start)
                .append(pass)
                .append(pass_1)
                .append(fail)
                .append(fail_1)
                .append(todo)
                .append(todo_1)
                .append(end)
        return sb
    }
}
