<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'bug.label', default: 'Bug')}"/>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<a href="#show-bug" class="skip" tabindex="-1">
    <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
</a>
<g:render template="/shared/projectButtonsTemplate"/>
<div id="show-bug" class="content scaffold-show" role="main">
    <h1>
        <g:message code="default.show.label" args="[entityName]"/>
    </h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list bug">
        <li class="fieldcontain">
            <span id="person-label" class="property-label">Created By</span>
            <div class="property-value" id="person">${bug.person}</div>
        </li>
        <li class="fieldcontain">
            <span id="project-label" class="property-label">Project</span>
            <div class="property-value" id="project">${bug.project.name}</div>
        </li>
        <li class="fieldcontain">
            <span id="area-label" class="property-label">Area</span>
            <div class="property-value" id="area">${bug.area?.name}</div>
        </li>
        <li class="fieldcontain" id="environments">
            <span id="environments-label" class="property-label">Environments</span>
            <g:each in="${bug?.environments}">
                <div class="property-value" aria-labelledby="environments-label">${it.name}</div>
            </g:each>
        </li>
        <li class="fieldcontain">
            <span id="name-label" class="property-label">Name</span>
            <div class="property-value" id="name">${bug.name}</div>
        </li>
        <li class="fieldcontain">
            <span id="description-label" class="property-label">Description</span>
            <div class="property-value" id="description">${bug.description}</div>
        </li>
        <li class="fieldcontain">
            <span id="platform-label" class="property-label">Platform</span>
            <div class="property-value" id="platform">${bug.platform}</div>
        </li>
    </ol>
    <g:render template="/shared/showStepsTableTemplate" bean="${bug}" var="entity"/>
    <sec:ifAnyGranted roles="ROLE_BASIC">
        <g:form resource="${this.bug}" method="DELETE" uri="/project/${bug.project.id}/bug/delete/${bug.id}">
            <fieldset class="buttons">
                <g:link class="edit" uri="/project/${bug.project.id}/bug/edit/${bug.id}" data-test-id="show-edit-link">
                    <g:message code="default.button.edit.label" default="Edit"/>
                </g:link>
                <input class="delete" type="submit" data-test-id="show-delete-link"
                       value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                       onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
            </fieldset>
        </g:form>
    </sec:ifAnyGranted>
</div>
<asset:javascript src="popper.min.js"/>
</body>
</html>
