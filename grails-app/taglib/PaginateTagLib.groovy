import grails.artefact.TagLibrary
import grails.gsp.TagLib
import grails.util.TypeConvertingMap
import groovy.transform.CompileStatic
import org.grails.taglib.TagOutput
import org.grails.taglib.encoder.OutputContextLookupHelper

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
     * @attr offset Used only if params.offset is empty
     */
    Closure pagination = { Map attrsMap ->
        TypeConvertingMap attrs = (TypeConvertingMap)attrsMap
        def writer = out
        if (attrs.total == null) {
            throwTagError("Tag [paginate] is missing required attribute [total]")
        }

        def total = attrs.int('total') ?: 0
        def offset = attrs.int('offset') ?: params.int('offset') ?: 0
        def max = params.int('max')
        def maxsteps = (attrs.int('maxsteps') ?: 10) //TODO: does this work?
        if (!max) max = (attrs.int('max') ?: 10)

        Map linkParams = [:]
        if (attrs.params instanceof Map) linkParams.putAll((Map)attrs.params)
        linkParams.offset = offset - max
        linkParams.max = max
        if (params.sort) linkParams.sort = params.sort
        if (params.order) linkParams.order = params.order

        Map linkTagAttrs = [:]
        linkTagAttrs.params = linkParams

        // determine paging variables
        def steps = maxsteps > 0
        int currentstep = ((offset / max) as int) + 1
        int firststep = 1
        int laststep = Math.round(Math.ceil((total / max) as double)) as int

        // display previous link when not on firststep
        if (currentstep > firststep) {
            writer << "<li class=\"page-item\"><a class=\"page-link\" href=\"/projects?offset=${offset - max}&max=10\">Previous</a></li>"
        }

        //TODO: need to add max variable for each link
        //TODO: add private function to create links

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
                //linkParams.offset = 0
                //writer << callLink((Map)linkTagAttrs.clone()) {firststep.toString()}
                //writer << "<a href=\"/projects?offset=0&max=10\">${firststep.toString()}</a>"
                writer << "<li class=\"page-item\"><a class=\"page-link\" href=\"/projects?offset=0&max=10\">${firststep.toString()}</a></li>"
            }
            //show a gap if beginstep isn't immediately after firststep
            if (beginstep > firststep+1) { //TODO: why doesn't this work?
                writer << '<span class="step gap">..</span>'
            }

            // display paginate steps
            (beginstep..endstep).each { int i ->
                if (currentstep == i) {
                    writer << "<li class=\"page-item active\"><span class=\"page-link\">${i}</span></li>"
                }
                else {
                    //linkParams.offset = (i - 1) * max
                    //writer << callLink((Map)linkTagAttrs.clone()) {i.toString()}
                    //writer << "<a href=\"/projects?offset=${(i - 1) * max}&max=10\">${i.toString()}</a>"
                    writer << "<li class=\"page-item\"><a class=\"page-link\" href=\"/projects?offset=${(i - 1) * max}&max=10\">${i.toString()}</a></li>"
                }
            }

            //show a gap if beginstep isn't immediately before firststep
            if (endstep+1 < laststep) { //TODO: why doesn't this work?
                writer << '<span class="step gap">..</span>'
            }
            // display laststep link when endstep is not laststep
            if (endstep < laststep) {
                //linkParams.offset = (laststep - 1) * max
                //writer << callLink((Map)linkTagAttrs.clone()) { laststep.toString() }
                //writer << "<a href=\"/projects?offset=${(laststep - 1) * max}&max=10\">${laststep.toString()}</a>"
                writer << "<li class=\"page-item\"><a class=\"page-link\" href=\"/projects?offset=${(laststep - 1) * max}&max=10\">${laststep.toString()}</a></li>"
            }
        }

        // display next link when not on laststep
        if (currentstep < laststep) {
            //linkTagAttrs.put('class', 'nextLink')
            //linkParams.offset = offset + max
            //writer << "<a href=\"/projects?offset=${offset + max}&max=10\">Next</a>"
            writer << "<li class=\"page-item\"><a class=\"page-link\" href=\"/projects?offset=${offset + max}&max=10\">Next</a></li>"
        }
    }

    private callLink(Map attrs, Object body) {
        TagOutput.captureTagOutput(tagLibraryLookup, 'g', 'link', attrs, body,
                OutputContextLookupHelper.lookupOutputContext())
    }
}
