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

    void "multiProgressBar tag throws error when unexecuted not provided"() {
        when:
        tagLib.multiProgressBar(total:0)

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [multiProgressBar] is missing required attribute [unexecuted]"
    }

    void "multiProgressBar multiProgressBar tag throws error when passed not provided"() {
        when:
        tagLib.multiProgressBar(total:0, unexecuted:0)

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [multiProgressBar] is missing required attribute [passed]"
    }

    void "multiProgressBar multiProgressBar tag throws error when failed not provided"() {
        when:
        tagLib.multiProgressBar(total:0, unexecuted:0, passed:0)

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [multiProgressBar] is missing required attribute [failed]"
    }

    void "multiProgressBar multiProgressBar tag throws error when skipped not provided"() {
        when:
        tagLib.multiProgressBar(total:0, unexecuted:0, passed:0, failed: 0)

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [multiProgressBar] is missing required attribute [skipped]"
    }

    void "multiProgressBar tag outputs empty string if values are greater than total"() {
        when:
        def found =
                applyTemplate("<g:multiProgressBar total='2' unexecuted='5' passed='5' failed='5' skipped='5'/>")

        then:
        found == ''
    }

    void "multiProgressBar tag outputs correct numbers when value is null"() {
        when:
        def found =
                applyTemplate("<g:multiProgressBar total='2' unexecuted='null' passed='0' failed='2' skipped='0'/>")

        then:
        found == createHtml(0, 0.00, 2, 100.00, 0, 0.00, 0, 0.00)
    }

    void "multiProgressBar tag outputs empty string when total is null"() {
        when:
        def found =
                applyTemplate("<g:multiProgressBar total='null' unexecuted='0' passed='0' failed='2' skipped='0'/>")

        then:
        found == ''
    }

    void "multiProgressBar tag outputs empty string when value count is less than total"() {
        when:
        def found =
                applyTemplate("<g:multiProgressBar total='10' unexecuted='0' passed='0' failed='2' skipped='0'/>")

        then:
        found == ''
    }

    void "multiProgressBar tag outputs correct numbers"(int total, int passed, double passPercent, int failed, double failPercent,
                                       int unexecuted, double unexecutedPercent, int skipped, double skipPercent) {
        when:
        def found =
                applyTemplate("<g:multiProgressBar total='${total}' unexecuted='${unexecuted}' passed='${passed}' failed='${failed}' skipped='${skipped}'/>")

        then:
        found == createHtml(passed, passPercent, failed, failPercent, unexecuted, unexecutedPercent, skipped, skipPercent)

        where:
        total | passed | passPercent | failed | failPercent | unexecuted | unexecutedPercent | skipped | skipPercent
        0     | 0      | 0.00        | 0      | 0.00        | 0          | 0.00              | 0       | 0.00
        12    | 2      | 16.67       | 6      | 50.00       | 3          | 25.00             | 1       | 8.33
        10    | 0      | 0           | 0      | 0           | 10         | 100               | 0       | 0.00
        8     | 2      | 25.00       | 2      | 25.00       | 2          | 25.00             | 2       | 25.00
    }

    void "failureCause tag throws error when value not provided"() {
        when:
        tagLib.failureCause()

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [failureCause] is missing required attribute [value]"
    }

    void "failureCause replaces line breaks with html breaks"() {
        when:
        def found = applyTemplate("<g:failureCause value='testing \n should be replaced with html break'/>")


        then:
        found == 'testing <br /> should be replaced with html break'
    }

    private String createHtml(int passed, double passPercent, int failed, double failPercent,
                              int todoCount, double todoPercent, int skipped, double skipPercent) {
        def sb = new StringBuilder()

        final DecimalFormat df = new DecimalFormat("0.00");

        def start = "<div class='progress-stacked w-50'>"
        def pass = "<div class='progress' role='progressbar' aria-label='passed' aria-valuenow='${passed}'"
        def pass_1 = " aria-valuemin='0' aria-valuemax='100' style='width: ${df.format(passPercent)}%'><div class='progress-bar bg-success'></div></div>"
        def fail = "<div class='progress' role='progressbar' aria-label='failed' aria-valuenow='${failed}'"
        def fail_1 =
                " aria-valuemin='0' aria-valuemax='100' style='width: ${df.format(failPercent)}%'><div class='progress-bar bg-danger'></div></div>"
        def skip = "<div class='progress' role='progressbar' aria-label='skipped' aria-valuenow='${skipped}'"
        def skip_1 =
                " aria-valuemin='0' aria-valuemax='100' style='width: ${df.format(skipPercent)}%'><div class='progress-bar bg-warning'></div></div>"
        def todo = "<div class='progress' role='progressbar' aria-label='todo' aria-valuenow='${todoCount}'"
        def todo_1 =
                " aria-valuemin='0' aria-valuemax='100' style='width: ${df.format(todoPercent)}%'><div class='progress-bar bg-secondary'></div></div>"
        def end = '</div>'
        sb.append(start)
                .append(pass)
                .append(pass_1)
                .append(fail)
                .append(fail_1)
                .append(skip)
                .append(skip_1)
                .append(todo)
                .append(todo_1)
                .append(end)
        return sb
    }
}
