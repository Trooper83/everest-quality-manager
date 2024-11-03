package com.manager.quality.everest.taglibs

import grails.artefact.TagLibrary
import grails.gsp.TagLib
import grails.util.TypeConvertingMap
import groovy.transform.CompileStatic

@TagLib
@CompileStatic
class TableTagLib implements TagLibrary {

    /**
     * pagination and sortableColumn taken from gsp paginate closure found:
     * https://github.com/grails/grails-gsp/blob/5.3.x/grails-plugin-gsp/src/main/groovy/org/grails/plugins/web/taglib/UrlMappingTagLib.groovy
    */

    /**
     * Creates next/previous links to support pagination for the current controller.<br/>
     *
     * &lt;g:paginate total="${Project.count()}" /&gt;<br/>
     *
     * @emptyTag
     *
     * @attr total REQUIRED The total number of results to paginate
     * @attr max The number of records displayed per page (defaults to 10). Used ONLY if params.max is empty
     * @attr maxsteps The number of steps displayed for pagination (defaults to 10). Used ONLY if params.maxsteps is empty
     * @attr projectId Id of the project
     * @attr domain REQUIRED The domain of the item
     * @attr isSearch Determines if the link should include isSearch parameter
     * @attr isTopLevel Determines if the link is for a top-level domain [true, false] defaults to true
     * @attr itemId The id of item to add to the link
     */
    Closure pagination = { Map attrsMap ->
        TypeConvertingMap attrs = (TypeConvertingMap)attrsMap
        def writer = out
        if (attrs.total == null) {
            throwTagError("Tag [pagination] is missing required attribute [total]")
        }

        def projectId = attrs.int('projectId')
        def domainAttr = attrs.getProperty('domain')
        def itemId = attrs.int('itemId') ?: -1
        def isTopLevel = attrs.boolean('isTopLevel') == null ? true : attrs.boolean('isTopLevel')

        if (!domainAttr) {
            throwTagError("Tag [pagination] requires [domain]")
        }

        def domain = domainAttr.toString()

        def projectOrUserDomain = true

        if (domain != 'project' & domain != 'user') {
            projectOrUserDomain = false
        }

        if (!projectOrUserDomain && !projectId) {
            throwTagError("Tag [pagination] requires [projectId] for non-project domains")
        }

        if (!isTopLevel && itemId == -1) {
            throwTagError("Tag [pagination] requires [itemId] for non-top level views")
        }

        projectId = projectId ?: -1
        def total = attrs.int('total') ?: 0
        def offset = params.int('offset') ?: 0
        def max = params.int('max')
        def maxsteps = (attrs.int('maxsteps') ?: 10)
        if (!max) max = (attrs.int('max') ?: 10)

        Map linkParams = [:]
        linkParams.max = max
        if (params.sort) linkParams.sort = params.sort
        if (params.order) linkParams.order = params.order
        if (params.isSearch) {
            linkParams.isSearch = params.isSearch
            linkParams.searchTerm = params.searchTerm
        }

        // determine paging variables
        def steps = maxsteps > 0
        int currentstep = ((offset / max) as int) + 1
        int firststep = 1
        int laststep = Math.round(Math.ceil((total / max) as double)) as int

        // display previous link when not on firststep
        if (currentstep > firststep) {
            linkParams.offset = offset - max
            def href = createHref(linkParams, domain, projectId, isTopLevel, itemId)
            def link = "<li class=\"page-item\"><a class=\"page-link\" href=${href}>Previous</a></li>"
            writer << link
        }

        // display steps when steps are enabled and laststep is not firststep
        if (steps && laststep > firststep) {

            // determine begin and endstep paging variables
            int beginstep = currentstep - (Math.round(maxsteps / 2.0d) as int) + (maxsteps % 2)
            int endstep = currentstep + (Math.round(maxsteps / 2.0d) as int) - 1

            if (beginstep < firststep) {
                beginstep = firststep
                endstep = maxsteps
            }
            if (endstep > laststep) {
                beginstep = laststep - maxsteps + 1
                if (beginstep < firststep) {
                    beginstep = firststep
                }
                endstep = laststep
            }

            // display firststep link when beginstep is not firststep
            if (beginstep > firststep) {
                linkParams.offset = 0
                def href = createHref(linkParams, domain, projectId, isTopLevel, itemId)
                def link = "<li class=\"page-item\"><a class=\"page-link\" href=${href}>${firststep.toString()}</a></li>"
                writer << link
            }
            //show a gap if beginstep isn't immediately after firststep
            if (beginstep > firststep+1) {
                writer << "<li class=\"page-item disabled\"><span class=\"page-link\">...</span></li>"
            }

            // display paginate steps
            (beginstep..endstep).each { int i ->
                if (currentstep == i) {
                    writer << "<li class=\"page-item active\"><span class=\"page-link\">${i}</span></li>"
                }
                else {
                    linkParams.offset = (i-1) * max
                    def href = createHref(linkParams, domain, projectId, isTopLevel, itemId)
                    def link = "<li class=\"page-item\"><a class=\"page-link\" href=${href}>${i.toString()}</a></li>"
                    writer << link
                }
            }

            //show a gap if beginstep isn't immediately before firststep
            if (endstep+1 < laststep) {
                writer << "<li class=\"page-item disabled\"><span class=\"page-link\">...</span></li>"
            }
            // display laststep link when endstep is not laststep
            if (endstep < laststep) {
                linkParams.offset = (laststep - 1) * max
                def href = createHref(linkParams, domain, projectId, isTopLevel, itemId)
                def link = "<li class=\"page-item\"><a class=\"page-link\" href=${href}>${laststep.toString()}</a></li>"
                writer << link
            }
        }

        // display next link when not on laststep
        if (currentstep < laststep) {
            linkParams.offset = offset + max
            def href = createHref(linkParams, domain, projectId, isTopLevel, itemId)
            def link = "<li class=\"page-item\"><a class=\"page-link\" href=${href}>Next</a></li>"
            writer << link
        }
    }

    /**
     * Renders a sortable column to support sorting in list views.<br/>
     *
     * Attribute title is required. When both attributes are specified then titleKey takes precedence,
     * resulting in the title caption to be resolved against the message source. In case when the message could
     * not be resolved, the title will be used as title caption.<br/>
     *
     * Examples:<br/>
     *
     * &lt;g:columnSort property="title" title="Title" /&gt;<br/>
     * &lt;g:columnSort property="releaseDate" defaultOrder="desc" title="Release Date" /&gt;<br/>
     *
     * @emptyTag
     *
     * @attr property - name of the property relating to the field
     * @attr defaultOrder default order for the property; choose between asc (default if not provided) and desc
     * @attr title title caption for the column
     * @attr domain name of the domain REQUIRED
     * @attr projectId id of the project REQUIRED
     */
    Closure columnSort = { Map attrsMap ->
        TypeConvertingMap attrs = (TypeConvertingMap)attrsMap
        def writer = out
        if (!attrs.property) {
            throwTagError("Tag [columnSort] is missing required attribute [property]")
        }

        if (!attrs.title) {
            throwTagError("Tag [columnSort] is missing required attribute [title]")
        }

        def domainAttr = attrs.getProperty('domain')
        def domain = domainAttr.toString()
        def itemId = attrs.int('itemId') ?: -1
        def isTopLevel = attrs.boolean('isTopLevel') == null ? true : attrs.boolean('isTopLevel')
        def projectId = attrs.int('projectId')

        if (!domainAttr) {
            throwTagError("Tag [columnSort] requires [domain]")
        }

        def projectOrUserDomain = true

        if (domain != 'project' & domain != 'user') {
            projectOrUserDomain = false
        }

        if (!projectOrUserDomain && !projectId) {
            throwTagError("Tag [columnSort] requires [projectId] for non-project domains")
        }

        if (!isTopLevel && itemId == -1) {
            throwTagError("Tag [columnSort] requires [itemId] for non-top level views")
        }

        projectId = projectId ?: -1

        def property = attrs.remove("property")

        def defaultOrder = attrs.remove("defaultOrder")
        if (defaultOrder != "desc") defaultOrder = "asc"

        // current sorting property and order
        def sort = params.sort
        def order = params.order

        // add sorting property and params to link params
        Map linkParams = [:]
        if (params.isSearch) {
            linkParams.isSearch = params.isSearch
            linkParams.searchTerm = params.searchTerm
        }
        linkParams.sort = property

        // propagate "max" and "offset" standard params
        if (params.max) linkParams.max = params.max
        if (params.offset) linkParams.offset = params.offset

        // determine and add sorting order for this column to link params
        attrs['class'] = (attrs['class'] ? "${attrs['class']} sortable" : "sortable")
        if (property == sort) {
            attrs['class'] = (attrs['class'] as String) + " sorted " + order
            if (order == "asc") {
                linkParams.order = "desc"
            }
            else {
                linkParams.order = "asc"
            }
        }
        else {
            linkParams.order = defaultOrder
        }

        // determine column title
        String title = attrs.remove("title") as String

        def href = createHref(linkParams, domain, projectId, isTopLevel, itemId)
        def link = "<th class=\"${attrs['class']}\"><a href=\"${href}\">${title}</a></th>"
        writer << link
    }

    private String createHref(Map linkParams, String domain, int projectId, boolean isTopLevel, int itemId) {
        def p = ""
        def s = linkParams.size()
        linkParams.eachWithIndex { key, value, i ->
            if(i==0) {
                p = "?"
            }
            p = p + "$key=$value"
            if(i != s - 1) {
                p = p + "&"
            }
        }
        def href
        if (domain == 'project') {
            href = "/projects"
        } else if (domain == 'user') {
            href = '/user/search'
        } else if (!isTopLevel) {
            href = "/project/${projectId}/${domain}/show/${itemId}"
        }
        else {
            href = "/project/${projectId}/${domain}s"
        }
        return href + p
    }
}
