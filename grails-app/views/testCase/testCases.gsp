<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testCase.label', default: 'TestCase')}"/>
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
<a href="#list-testCase" class="skip" tabindex="-1">
    <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
</a>
<div class="row justify-content-end mt-3">
    <sec:ifAnyGranted roles="ROLE_BASIC">
        <g:if test="${params.projectId}">
            <g:link role="button" class="btn btn-secondary" elementId="createButton" uri="/project/${params.projectId}/testCase/create">Create Test</g:link>
        </g:if>
    </sec:ifAnyGranted>
</div>
<div id="list-testCase" class="content scaffold-list col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
    <h1>
        <g:message code="default.list.label" args="[entityName]"/>
    </h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <div class="col-4 mt-4 mb-4">
        <g:form uri="/project/${project.id}/testCases" params="['isSearch': 'true']" useToken="true">
            <g:textField name="name" autocomplete="off" placeholder="Test Case Name" />
            <button class="btn btn-secondary" type="submit" id="searchButton">Search</button>
        </g:form>
    </div>
    <table>
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