package com.everlution

import grails.artefact.TagLibrary
import grails.gsp.TagLib
import grails.util.TypeConvertingMap
import groovy.transform.CompileStatic

@TagLib
@CompileStatic
class PaginateTagLib implements TagLibrary {

    /**
     * pagination taken from gsp paginate closure found:
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
     * @attr projectId id of the project
     * @attr domain The domain of the item
     * @attr isSearch Determines if the link should include isSearch parameter
     */
    Closure pagination = { Map attrsMap ->
        TypeConvertingMap attrs = (TypeConvertingMap)attrsMap
        def writer = out
        if (attrs.total == null) {
            throwTagError("Tag [pagination] is missing required attribute [total]")
        }

        def projectId = attrs.int('projectId')
        def domainAttr = attrs.getProperty('domain')

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
            linkParams.name = params.name
        }

        // determine paging variables
        def steps = maxsteps > 0
        int currentstep = ((offset / max) as int) + 1
        int firststep = 1
        int laststep = Math.round(Math.ceil((total / max) as double)) as int

        // display previous link when not on firststep
        if (currentstep > firststep) {
            linkParams.offset = offset - max
            writer << createLink(linkParams, 'Previous', domain, projectId)
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
                writer << createLink(linkParams, firststep.toString(), domain, projectId)
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
                    writer << createLink(linkParams, i.toString(), domain, projectId)
                }
            }

            //show a gap if beginstep isn't immediately before firststep
            if (endstep+1 < laststep) {
                writer << "<li class=\"page-item disabled\"><span class=\"page-link\">...</span></li>"
            }
            // display laststep link when endstep is not laststep
            if (endstep < laststep) {
                linkParams.offset = (laststep - 1) * max
                writer << createLink(linkParams, laststep.toString(), domain, projectId)
            }
        }

        // display next link when not on laststep
        if (currentstep < laststep) {
            linkParams.offset = offset + max
            writer << createLink(linkParams, 'Next', domain, projectId)
        }
    }

    private String createLink(Map linkParams, String linkText, String domain, int projectId) {
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
        } else {
            href = "/project/${projectId}/${domain}s"
        }
        href = href + p
        def link = "<li class=\"page-item\"><a class=\"page-link\" href=${href}>${linkText}</a></li>"
        return link
    }
}
