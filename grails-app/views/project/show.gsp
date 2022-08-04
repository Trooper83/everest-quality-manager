<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
    <title>Show Project</title>
</head>
<body>
<g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
<div id="show-project" class="content scaffold-show col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
    <g:render template="/shared/projectButtonsTemplate"/>
    <h1>Show Project</h1>
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
    <sec:ifAnyGranted roles="ROLE_PROJECT_ADMIN">
        <g:form resource="${this.project}" method="DELETE">
            <fieldset class="buttons">
                <g:link class="edit" action="edit" resource="${this.project}" data-test-id="home-edit-link">
                    <g:message code="default.button.edit.label" default="Edit" />
                </g:link>
                <input class="delete" type="submit" data-test-id="home-delete-link" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
            </fieldset>
        </g:form>
    </sec:ifAnyGranted>
</div>
<asset:javascript src="popper.min.js"/>
</body>
</html>