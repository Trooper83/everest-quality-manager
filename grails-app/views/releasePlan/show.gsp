<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'releasePlan.label', default: 'ReleasePlan')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#show-releasePlan" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
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
