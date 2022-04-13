<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'testGroup.label', default: 'TestGroup')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#show-testGroup" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <g:render template="/shared/showPageNavigationTemplate"/>
        <div id="show-testGroup" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${testGroup}" var="entity"/>
            <ol class="property-list testGroup">
                <li class="fieldcontain">
                    <span id="project-label" class="property-label">Project</span>
                    <div class="property-value" id="project">${testGroup.project.name}</div>
                </li>
                <li class="fieldcontain">
                    <span id="name-label" class="property-label">Name</span>
                    <div class="property-value" id="name">${testGroup.name}</div>
                </li>
            </ol>
                <f:table collection="${testGroup.testCases}" order="id, name, area, platform, environments, type, executionMethod"/>
                <div class="pagination">
                    <g:paginate total="${testGroup.testCases.size() ?: 0}"/>
                </div>
            <sec:ifAnyGranted roles="ROLE_BASIC">
                <g:form resource="${this.testGroup}" method="DELETE" params="[projectId: this.testGroup.project.id]">
                    <fieldset class="buttons">
                        <g:link class="edit" uri="/project/${this.testGroup.project.id}/testGroup/edit/${this.testGroup.id}" data-test-id="show-edit-link">
                            <g:message code="default.button.edit.label" default="Edit"/>
                        </g:link>
                        <input class="delete" type="submit" data-test-id="show-delete-link"
                               value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                               onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
                    </fieldset>
                </g:form>
            </sec:ifAnyGranted>
        </div>
    </body>
</html>
