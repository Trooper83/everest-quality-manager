<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
    <title>Project Home</title>
</head>
<body>
<div class="container">
    <div class="row justify-content-end">
        <div class="col-1">
            <sec:ifAnyGranted roles="ROLE_BASIC">
                <div class="dropdown">
                    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Create
                    </button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                        <g:link class="dropdown-item" uri="/project/${project.id}/bug/create">Bug</g:link>
                        <g:link class="dropdown-item" controller="releasePlan" action="create">Release Plan</g:link>
                        <g:link class="dropdown-item" controller="scenario" action="create">Scenario</g:link>
                        <g:link class="dropdown-item" controller="testCase" action="create">Test Case</g:link>
                    </div>
                </div>
            </sec:ifAnyGranted>
        </div>
    </div>
</div>
<div class="container">
    <div id="show-project" class="content scaffold-show" role="main">
        <h1><g:message code="default.show.label" args="[entityName]" /></h1>
        <g:render template="/shared/messagesTemplate" bean="${project}" var="entity"/>
        <ol class="property-list">
            <li class="fieldcontain">
                <span id="name-label" class="property-label">Name</span>
                <div id="name" class="property-value" aria-labelledby="property-label">${project.name}</div>
            </li>
            <li class="fieldcontain">
                <span id="code-label" class="property-label">Code</span>
                <div id="code" class="property-value" aria-labelledby="code-label">${project.code}</div>
            </li>
            <li class="fieldcontain" id="areas">
                <span id="areas-label" class="property-label">Areas</span>
                <g:each in="${project.areas}">
                    <div class="property-value" aria-labelledby="areas-label">${it.name}</div>
                </g:each>
            </li>
            <li class="fieldcontain" id="environments">
                <span id="environments-label" class="property-label">Environments</span>
                <g:each in="${project.environments}">
                    <div class="property-value" aria-labelledby="environments-label">${it.name}</div>
                </g:each>
            </li>
        </ol>
    </div>
</div>
<asset:javascript src="popper.min.js"/>
</body>
</html>