<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'bug.label', default: 'Bug')}"/>
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<a href="#list-bug" class="skip" tabindex="-1">
    <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
</a>
<g:render template="/shared/projectButtonsTemplate"/>
<div id="list-bug" class="content scaffold-list" role="main">
    <h1>
        <g:message code="default.list.label" args="[entityName]"/>
    </h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Person</th>
            <th>Project</th>
            <th>Platform</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <g:each var="bug" in="${bugList}">
            <tr>
                <td><g:link uri="/project/${project.id}/bug/show/${bug.id}">${bug.name}</g:link></td>
                <td>${bug.description}</td>
                <td>${bug.person.email}</td>
                <td>${bug.project.name}</td>
                <td>${bug.platform}</td>
                <td>${bug.status}</td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${bugCount ?: 0}"/>
    </div>
</div>
<asset:javascript src="popper.min.js"/>
</body>
</html>