<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}"/>
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
<a href="#create-project" class="skip" tabindex="-1">
    <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
</a>
<div class="nav" role="navigation">
    <ul>
        <li>
            <a class="home" href="${createLink(uri: '/')}" data-test-id="create-home-link">
                <g:message code="default.home.label"/>
            </a>
        </li>
        <li>
            <g:link class="list" action="index" data-test-id="create-list-link">
                <g:message code="default.list.label" args="[entityName]"/>
            </g:link>
        </li>
    </ul>
</div>
<div id="create-project" class="content scaffold-create" role="main">
    <h1>
        <g:message code="default.create.label" args="[entityName]"/>
    </h1>
    <g:render template="/shared/messagesTemplate" bean="${project}" var="entity"/>
    <g:form resource="${this.project}" method="POST">
        <fieldset class="form">
            <f:all bean="project" except="areas, bugs, environments, testCases"/>
            <div class="fieldcontain" id="areas">
                <label>Areas</label>
                <g:field type="text" name="area" data-toggle="tooltip" trigger="manual" title="Area Name cannot be blank"/>
                <g:field type="button" name="btnAddArea" value="Add" onclick="addAreaTag()"/>
                <ul class="one-to-many"></ul>
            </div>
            <div class="fieldcontain" id="environments">
                <label>Environments</label>
                <g:field type="text" name="environment" data-toggle="tooltip" trigger="manual" title="Environment Name cannot be blank"/>
                <g:field type="button" name="btnAddEnvironment" value="Add" onclick="addEnvironmentTag()"/>
                <ul class="one-to-many"></ul>
            </div>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}"/>
        </fieldset>
    </g:form>
</div>
<asset:javascript src="project.js"/>
<asset:javascript src="popper.min.js"/>
</body>
</html>
