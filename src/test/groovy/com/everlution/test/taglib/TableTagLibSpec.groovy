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
        def e = thrown(GrailsTagException)
        e.message == "Tag [pagination] is missing required attribute [total]"
    }

    void "pagination throws error when domain not provided"() {
        when:
        tagLib.pagination(total:0)

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [pagination] requires [domain]"
    }

    void "pagination throws error when projectId not provided for non-project domains"() {
        when:
        tagLib.pagination(total:0, domain:'bug')

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [pagination] requires [projectId] for non-project domains"
    }

    void "pagination throws error when itemId not provided for non-top level views"() {
        when:
        tagLib.pagination(total:0, domain:'bug', projectId:1, isTopLevel:false)

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [pagination] requires [itemId] for non-top level views"
    }

    void "pagination does not throw error when itemId not provided for top level views"() {
        when:
        tagLib.pagination(total:0, domain:'bug', projectId:1, isTopLevel:true)

        then:
        notThrown(GrailsTagException)
    }

    void "tag not rendered when total less than 11"(String total) {
        when:
        def found = applyTemplate("<g:pagination domain='bug' projectId='1' total='${total}'/>")

        then:
        found == ''

        where:
        total << ['1', '10']
    }

    void "tag rendered when total greater than 10"() {
        when:
        def found = applyTemplate("<g:pagination domain='bug' projectId='1' total='11'/>")

        then:
        found == '<li class="page-item active"><span class="page-link">1</span></li>' +
                 '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=10>2</a></li>' +
                 '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=10>Next</a></li>'
    }

    void "max set to 10 when not provided"() {
        when:
        def found = applyTemplate("<g:pagination domain='bug' projectId='1' total='11'/>")

        then:
        found == '<li class="page-item active"><span class="page-link">1</span></li>' +
                '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=10>2</a></li>' +
                '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=10>Next</a></li>'
    }

    void "tag renders 6 pages when total greater than 50"() {
        when:
        def found = applyTemplate("<g:pagination domain='bug' projectId='1' total='51'/>")

        then:
        found == '<li class="page-item active"><span class="page-link">1</span></li>' +
                '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=10>2</a></li>' +
                '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=20>3</a></li>' +
                '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=30>4</a></li>' +
                '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=40>5</a></li>' +
                '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=50>6</a></li>' +
                '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=10>Next</a></li>'
    }

    void "previous button present when not on first page"() {
        when:
        params.offset = 10
        def found = applyTemplate("<g:pagination domain='bug' projectId='1' total='11'/>")

        then:
        found == '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=0>Previous</a></li>' +
                '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=0>1</a></li>' +
                '<li class="page-item active"><span class="page-link">2</span></li>'
    }

    void "next button not present when no more pages present"() {
        when:
        params.offset = 10
        def found = applyTemplate("<g:pagination domain='bug' projectId='1' total='11'/>")

        then:
        found == '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=0>Previous</a></li>' +
                '<li class="page-item"><a class="page-link" href=/project/1/bugs?max=10&offset=0>1</a></li>' +
                '<li class="page-item active"><span class="page-link">2</span></li>'
    }

    void "search params retained when present"() {
        when:
        params.isSearch = true
        params.name = 'search for me'
        def found = applyTemplate("<g:pagination domain='bug' projectId='1' total='11'/>")

        then:
        found.contains('isSearch=true')
        found.contains('name=search for me')
    }

    void "sort params retained when present"() {
        when:
        params.sort = 'name'
        params.order = 'desc'
        def found = applyTemplate("<g:pagination domain='bug' projectId='1' total='11'/>")

        then:
        found.contains('sort=name')
        found.contains('order=desc')
    }

    void "href is correct when project domain"() {
        when:
        def found = applyTemplate("<g:pagination domain='project' total='11'/>")

        then:
        found.contains('href=/projects')
    }

    void "href is correct when user domain"() {
        when:
        def found = applyTemplate("<g:pagination domain='user' total='11'/>")

        then:
        found.contains('href=/user/search')
    }

    void "href is correct when not project or user domain"() {
        when:
        def found = applyTemplate("<g:pagination projectId='1' domain='bug' total='11'/>")

        then:
        found.contains('href=/project/1/bugs')
    }

    void "href is correct when testGroup and TestCycle domain and not toplevel"(String domain) {
        when:
        def found = applyTemplate("<g:pagination projectId='1' itemId='1' domain='${domain}' total='11' isTopLevel='false'/>")

        then:
        found.contains("href=/project/1/${domain}/show/1")

        where:
        domain << ['testGroup', 'testCycle', 'testCase']
    }

    void "columnSort throws error when property attribute not provided"() {
        when:
        tagLib.columnSort()

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [columnSort] is missing required attribute [property]"
    }

    void "columnSort throws error when title attribute not provided"() {
        when:
        tagLib.columnSort(property:'test')

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [columnSort] is missing required attribute [title]"
    }

    void "columnSort throws error when domain attribute not provided"() {
        when:
        tagLib.columnSort(property:'test', title:'header')

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [columnSort] requires [domain]"
    }

    void "columnSort throws error when projectId not provided for non-project domains"() {
        when:
        tagLib.columnSort(property:'test', title:'header', domain:'bug')

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [columnSort] requires [projectId] for non-project domains"
    }

    void "columnSort throws error when itemId not provided for non-top level views"() {
        when:
        tagLib.columnSort(property:'test', title:'header', domain:'bug', projectId:1, isTopLevel:false)

        then:
        def e = thrown(GrailsTagException)
        e.message == "Tag [columnSort] requires [itemId] for non-top level views"
    }

    void "columnSort does not throw error when itemId not provided for top level views"() {
        when:
        tagLib.columnSort(property:'test', title:'header', domain:'bug', projectId:1, isTopLevel:true)

        then:
        notThrown(GrailsTagException)
    }

    void "order defaults to asc when not provided"() {
        when:
        def found = applyTemplate("<g:columnSort title='headers' property='testing' projectId='1' domain='bug'/>")

        then:
        found.contains("order=asc")
    }

    void "order inverts to desc when current is asc"() {
        when:
        params.order = 'asc'
        params.sort = 'testing'
        def found = applyTemplate("<g:columnSort title='headers' property='testing' projectId='1' domain='bug'/>")

        then:
        found.contains("order=desc")
    }

    void "search params are retained"() {
        when:
        params.order = 'asc'
        params.sort = 'testing'
        params.isSearch = 'true'
        params.name = 'searchName'
        def found = applyTemplate("<g:columnSort title='headers' property='testing' projectId='1' domain='bug'/>")

        then:
        found.contains("isSearch=true")
        found.contains("name=searchName")
    }

    void "pagination params are retained"() {
        when:
        params.order = 'asc'
        params.sort = 'testing'
        params.offset = '10'
        params.max = '20'
        def found = applyTemplate("<g:columnSort title='headers' property='testing' projectId='1' domain='bug'/>")

        then:
        found.contains("max=20")
        found.contains("offset=10")
    }

    void "columnSort href is correct when project domain"() {
        when:
        def found = applyTemplate("<g:columnSort title='headers' property='testing' domain='project'/>")

        then:
        found == '<th><a href=/projects?sort=testing&order=asc>headers</a></th>'
    }

    void "columnSort href is correct when user domain"() {
        when:
        def found = applyTemplate("<g:columnSort title='headers' property='testing' domain='user'/>")

        then:
        found == '<th><a href=/user/search?sort=testing&order=asc>headers</a></th>'
    }

    void "columnSort href is correct when not project or user domain"() {
        when:
        def found = applyTemplate("<g:columnSort title='headers' property='testing' domain='bug' projectId='1'/>")

        then:
        found == '<th><a href=/project/1/bugs?sort=testing&order=asc>headers</a></th>'
    }

    void "columnSort href is correct when testGroup and TestCycle domain and not toplevel"(String domain) {
        when:
        def found = applyTemplate("<g:columnSort projectId='1' itemId='1' domain='${domain}' isTopLevel='false' title='headers' property='testing'/>")

        then:
        found == "<th><a href=/project/1/${domain}/show/1?sort=testing&order=asc>headers</a></th>"

        where:
        domain << ['testGroup', 'testCycle', 'testCase']
    }
}
