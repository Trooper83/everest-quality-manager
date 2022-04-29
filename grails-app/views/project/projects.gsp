<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div id="list-project" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table class="table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Code</th>
        </tr>
        </thead>
        <tbody>
        <g:each var="project" in="${projectList}">
            <tr>
                <td><g:link uri="/project/home/${project.id}">${project.name}</g:link></td>
                <td>${project.code}</td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${projectCount ?: 0}" />
    </div>
</div>
</body>
</html>