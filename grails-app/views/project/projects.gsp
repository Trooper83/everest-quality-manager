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
    <g:if test="${flash.error}">
        <ul class="errors" role="alert">${flash.error}</ul>
    </g:if>
    <div class="col-4 mt-4 mb-4">
        <g:form action="projects" controller="project" params="['isSearch': 'true']">
            <g:textField name="name" autocomplete="off" placeholder="Project Name"></g:textField>
            <button class="btn btn-secondary" type="submit" id="searchButton">Search</button>
        </g:form>
    </div>
    <table>
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