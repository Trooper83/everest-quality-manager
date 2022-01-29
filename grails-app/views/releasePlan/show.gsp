<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'releasePlan.label', default: 'ReleasePlan')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#show-releasePlan" class="skip" tabindex="-1">
            <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
        </a>
        <g:render template="/shared/showPageNavigationTemplate"/>
        <div id="show-releasePlan" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <ol class="property-list releasePlan">
                <li class="fieldcontain">
                    <span id="name-label" class="property-label">Name</span>
                    <div class="property-value" id="name">${releasePlan.name}</div>
                </li>
                <li class="fieldcontain">
                    <span id="project-label" class="property-label">Project</span>
                    <div class="property-value" id="project">${releasePlan.project.name}</div>
                </li>
            </ol>
            <div class="container">
                <h1>Test Cycles</h1>
                <g:link class="btn btn-primary" controller="testCycle" action="create" params="['releasePlan.id': releasePlan.id]">Add Test Cycle</g:link>
            <div class="accordion" id="testCycles">
                <g:each status="i" var="cycle" in="${releasePlan.testCycles}">
                <div class="card">
                    <div class="card-header" id="testCycle-${i}">
                        <h2 class="mb-0">
                            <button class="btn btn-link btn-block text-left" type="button" data-toggle="collapse"
                                    data-target="#collapse-${i}">${cycle.name}
                            </button>
                        </h2>
                    </div>
                    <div id="collapse-${i}" class="collapse" data-parent="#testCycles">
                        <div class="card-body">
                            <div>
                                <label>Environment: </label><span>${cycle.environ?.name}</span>
                            </div>
                            <div>
                                <label>Platform: </label><span>${cycle.platform}</span>
                            </div>
                        </div>
                    </div>
                </div>
                </g:each>
            </div>
            </div>
            <sec:ifAnyGranted roles="ROLE_BASIC">
            <g:form resource="${this.releasePlan}" method="DELETE">
                <fieldset class="buttons">
                    <g:link class="edit" action="edit" resource="${this.releasePlan}" data-test-id="show-edit-link">
                        <g:message code="default.button.edit.label" default="Edit" />
                    </g:link>
                    <input class="delete" type="submit" data-test-id="show-delete-link" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </fieldset>
            </g:form>
            </sec:ifAnyGranted>
        </div>
    </body>
</html>
