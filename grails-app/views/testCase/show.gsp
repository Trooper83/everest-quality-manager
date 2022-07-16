<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testCase.label', default: 'TestCase')}"/>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<g:render template="/shared/sidebarTemplate" model="['name':testCase.project.name, 'code':testCase.project.code]"/>
<a href="#show-testCase" class="skip" tabindex="-1">
    <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
</a>
<g:render template="/shared/projectButtonsTemplate"/>
<div id="show-testCase" class="content scaffold-show col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
    <h1>
        <g:message code="default.show.label" args="[entityName]"/>
    </h1>
    <g:render template="/shared/messagesTemplate" bean="${testCase}" var="entity"/>
    <ol class="property-list testCase">
        <li class="fieldcontain">
            <span id="person-label" class="property-label">Created By</span>
            <div class="property-value" id="person">${testCase.person.email}</div>
        </li>
        <li class="fieldcontain">
            <span id="area-label" class="property-label">Area</span>
            <div class="property-value" id="area">${testCase.area?.name}</div>
        </li>
        <li class="fieldcontain" id="environments">
            <span id="environments-label" class="property-label">Environments</span>
            <g:each in="${testCase?.environments}">
                <div class="property-value" aria-labelledby="environments-label">${it.name}</div>
            </g:each>
        </li>
        <li class="fieldcontain" id="testGroups">
            <span id="testGroups-label" class="property-label">Test Groups</span>
            <g:each in="${testCase?.testGroups}">
                <div class="property-value" aria-labelledby="testGroups-label">${it.name}</div>
            </g:each>
        </li>
        <li class="fieldcontain">
            <span id="name-label" class="property-label">Name</span>
            <div class="property-value" id="name">${testCase.name}</div>
        </li>
        <li class="fieldcontain">
            <span id="description-label" class="property-label">Description</span>
            <div class="property-value" id="description">${testCase.description}</div>
        </li>
        <li class="fieldcontain">
            <span id="executionMethod-label" class="property-label">Execution Method</span>
            <div class="property-value" id="executionMethod">${testCase.executionMethod}</div>
        </li>
        <li class="fieldcontain">
            <span id="type-label" class="property-label">Type</span>
            <div class="property-value" id="type">${testCase.type}</div>
        </li>
        <li class="fieldcontain">
            <span id="platform-label" class="property-label">Platform</span>
            <div class="property-value" id="platform">${testCase.platform}</div>
        </li>
    </ol>
    <g:render template="/shared/showStepsTableTemplate" bean="${testCase}" var="entity"/>
    <sec:ifAnyGranted roles="ROLE_BASIC">
        <g:form resource="${this.testCase}" method="DELETE" uri="/project/${testCase.project.id}/testCase/delete/${testCase.id}">
            <fieldset class="buttons">
                <g:link class="edit" uri="/project/${testCase.project.id}/testCase/edit/${testCase.id}" data-test-id="show-edit-link">
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
