<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'scenario.label', default: 'Scenario')}"/>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<a href="#show-scenario" class="skip" tabindex="-1">
    <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
</a>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}" data-test-id="show-home-link">
            <g:message code="default.home.label"/>
        </a></li>
        <li>
            <g:link class="list" action="index" data-test-id="show-list-link">
                <g:message code="default.list.label" args="[entityName]"/>
            </g:link>
        </li>
        <sec:ifAnyGranted roles="ROLE_BASIC">
            <li>
                <g:link class="create" action="create" data-test-id="show-create-link">
                    <g:message code="default.new.label" args="[entityName]"/>
                </g:link>
            </li>
        </sec:ifAnyGranted>
    </ul>
</div>
<div id="show-scenario" class="content scaffold-show" role="main">
    <h1>
        <g:message code="default.show.label" args="[entityName]"/>
    </h1>
    <g:render template="/shared/messagesTemplate" bean="${scenario}" var="entity"/>
    <ol class="property-list scenario">
        <li class="fieldcontain">
            <span id="creator-label" class="property-label">Creator</span>
            <div class="property-value" id="creator">${scenario.creator}</div>
        </li>
        <li class="fieldcontain">
            <span id="project-label" class="property-label">Project</span>
            <div class="property-value" id="project">${scenario.project.name}</div>
        </li>
        <li class="fieldcontain">
            <span id="area-label" class="property-label">Area</span>
            <div class="property-value" id="area">${scenario.area?.name}</div>
        </li>
        <li class="fieldcontain" id="environments">
            <span id="environments-label" class="property-label">Environments</span>
            <g:each in="${scenario?.environments}">
                <div class="property-value" aria-labelledby="environments-label">${it.name}</div>
            </g:each>
        </li>
        <li class="fieldcontain">
            <span id="name-label" class="property-label">Name</span>
            <div class="property-value" id="name">${scenario.name}</div>
        </li>
        <li class="fieldcontain">
            <span id="description-label" class="property-label">Description</span>
            <div class="property-value" id="description">${scenario.description}</div>
        </li>
        <li class="fieldcontain">
            <span id="executionMethod-label" class="property-label">Execution Method</span>
            <div class="property-value" id="executionMethod">${scenario.executionMethod}</div>
        </li>
        <li class="fieldcontain">
            <span id="type-label" class="property-label">Type</span>
            <div class="property-value" id="type">${scenario.type}</div>
        </li>
        <li class="fieldcontain">
            <span id="gherkin-label" class="property-label">Gherkin</span>
            <div class="property-value" id="gherkin">${scenario.gherkin}</div>
        </li>
    </ol>
    <sec:ifAnyGranted roles="ROLE_BASIC">
        <g:form resource="${this.scenario}" method="DELETE">
            <fieldset class="buttons">
                <g:link class="edit" action="edit" resource="${this.scenario}" data-test-id="show-edit-link">
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