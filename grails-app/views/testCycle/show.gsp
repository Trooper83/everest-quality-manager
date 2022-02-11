<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'testCycle.label', default: 'TestCycle')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#show-testCycle" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="show-testCycle" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:link controller="releasePlan" action="show" id="${testCycle.releasePlan.id}">Back to Release Plan</g:link>
            <ol class="property-list testCycle">
                <li class="fieldcontain">
                    <span id="releasePlan-label" class="property-label">Release Plan</span>
                    <div class="property-value" id="releasePlan">${testCycle.releasePlan.name}</div>
                </li>
                <li class="fieldcontain">
                    <span id="name-label" class="property-label">Name</span>
                    <div class="property-value" id="name">${testCycle.name}</div>
                </li>
                <li class="fieldcontain">
                    <span id="environ-label" class="property-label">Environment</span>
                    <div class="property-value" id="environment">${testCycle.environ?.name}</div>
                </li>
                <li class="fieldcontain">
                    <span id="platform-label" class="property-label">Platform</span>
                    <div class="property-value" id="platform">${testCycle.platform}</div>
                </li>
            </ol>
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#iterationsModal">
                Add Tests
            </button>
            <f:table collection="${testCycle.testIterations}" order="id, name"/>
            <div class="pagination">
                <g:paginate total="${testCycle.testIterations.size() ?: 0}"/>
            </div>
            <sec:ifAnyGranted roles="ROLE_BASIC">
                <g:form resource="${this.testCycle}" method="DELETE">
                    <fieldset class="buttons">
                        <g:link class="edit" action="edit" resource="${this.testCycle}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                        <input class="delete" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    </fieldset>
                </g:form>
            </sec:ifAnyGranted>
        </div>
        <div class="modal fade" id="iterationsModal" tabindex="-1" aria-labelledby="iterationsModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="iterationsModalLabel">Add Tests</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary">Save changes</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
