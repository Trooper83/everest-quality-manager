package com.everlution.taglibs

import grails.artefact.TagLibrary
import grails.gsp.TagLib
import grails.util.TypeConvertingMap
import groovy.transform.CompileStatic

import java.text.DecimalFormat

@TagLib
@CompileStatic
class UtilityTagLib implements TagLibrary {

    /**
     * creates a multipoint progress bar
     *
     * &lt;g:multiProgressBar total="<total>" pass="<pass>"/&gt;<br/>
     *
     * @attr total REQUIRED The total number of results
     * @attr unexecuted REQUIRED the total number of unexecuted results
     * @attr failed REQUIRED the total number of failed results
     * @attr passed REQUIRED the total number of passed results
     * @attr skipped REQUIRED the total number of skipped results
     */
    Closure multiProgressBar = { Map attrsMap ->
        TypeConvertingMap attrs = (TypeConvertingMap) attrsMap
        def writer = out
        if (attrs.total == null) {
            throwTagError("Tag [multiProgressBar] is missing required attribute [total]")
        }
        if (attrs.unexecuted == null) {
            throwTagError("Tag [multiProgressBar] is missing required attribute [unexecuted]")
        }
        if (attrs.passed == null) {
            throwTagError("Tag [multiProgressBar] is missing required attribute [passed]")
        }
        if (attrs.failed == null) {
            throwTagError("Tag [multiProgressBar] is missing required attribute [failed]")
        }
        if (attrs.skipped == null) {
            throwTagError("Tag [multiProgressBar] is missing required attribute [skipped]")
        }

        def total = attrs.int('total') ?: 0
        def passed = attrs.int('passed') ?: 0
        def failed = attrs.int('failed') ?: 0
        def skipped = attrs.int('skipped') ?: 0
        def todoCount = attrs.int('unexecuted') ?: 0

        def sb = new StringBuilder()

        if (total == (passed + failed + todoCount + skipped)) {
            final DecimalFormat df = new DecimalFormat("0.00")

            double passPercent = 0
            double failPercent = 0
            double todoPercent = 0
            double skipPercent = 0

            if (total > 0) {
                passPercent = (passed / total) * 100
                failPercent = (failed / total) * 100
                skipPercent = (skipped / total) * 100
                todoPercent = (todoCount / total) * 100
            }

            def start = "<div class='progress-stacked w-50'>"
            def pass = "<div class='progress' role='progressbar' aria-label='passed' aria-valuenow='${passed}'"
            def pass_1 = " aria-valuemin='0' aria-valuemax='100' style='width: ${df.format(passPercent)}%'><div class='progress-bar bg-success'></div></div>"
            def fail = "<div class='progress' role='progressbar' aria-label='failed' aria-valuenow='${failed}'"
            def fail_1 =
                    " aria-valuemin='0' aria-valuemax='100' style='width: ${df.format(failPercent)}%'><div class='progress-bar bg-danger'></div></div>"
            def todo = "<div class='progress' role='progressbar' aria-label='todo' aria-valuenow='${todoCount}'"
            def todo_1 =
                    " aria-valuemin='0' aria-valuemax='100' style='width: ${df.format(todoPercent)}%'><div class='progress-bar bg-secondary'></div></div>"
            def skip = "<div class='progress' role='progressbar' aria-label='skipped' aria-valuenow='${skipped}'"
            def skip_1 = " aria-valuemin='0' aria-valuemax='100' style='width: ${df.format(skipPercent)}%'><div class='progress-bar bg-warning'></div></div>"
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
        } else {
            sb.append('')
        }

        writer << sb
    }

    /**
     * tag for retaining line breaks in failure cause of results
     *
     * &lt;g:failureCause value=<value>"/&gt;<br/>
     *
     * @attr value - the failureCause to render
     */
    Closure failureCause = { Map attrsMap ->
        TypeConvertingMap attrs = (TypeConvertingMap) attrsMap
        def writer = out
        if (attrs.value == null) {
            throwTagError("Tag [failureCause] is missing required attribute [value]")
        }

        def v = attrs.getProperty('value').toString()

        def vfmt = v.replace("\n", "<br />")

        writer << vfmt
    }
}
