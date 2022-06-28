<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testCase.label', default: 'TestCase')}"/>
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<a href="#list-testCase" class="skip" tabindex="-1">
    <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
</a>
<g:render template="/shared/projectButtonsTemplate"/>
<div id="list-testCase" class="content scaffold-list" role="main">
    <h1>
        <g:message code="default.list.label" args="[entityName]"/>
    </h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table class="table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Person</th>
            <th>Project</th>
            <th>Platform</th>
            <th>Type</th>
        </tr>
        </thead>
        <tbody>
        <g:each var="test" in="${testCaseList}">
            <tr>
                <td><g:link uri="/project/${project.id}/testCase/show/${test.id}">${test.name}</g:link></td>
                <td>${test.description}</td>
                <td>${test.person.email}</td>
                <td>${test.project.name}</td>
                <td>${test.platform}</td>
                <td>${test.type}</td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${testCaseCount ?: 0}"/>
    </div>
</div>
<asset:javascript src="popper.min.js"/>
</body>
</html>