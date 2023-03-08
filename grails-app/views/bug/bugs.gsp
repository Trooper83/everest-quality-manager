<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'bug.label', default: 'Bug')}"/>
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
<a href="#list-bug" class="skip" tabindex="-1">
    <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
</a>
<div id="list-bug" class="content scaffold-list col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
    <h1>
        <g:message code="default.list.label" args="[entityName]"/>
    </h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <div class="col-4 mt-4 mb-4">
        <g:form uri="/project/${project.id}/bugs" params="['isSearch': 'true']" useToken="true">
            <g:textField name="name" autocomplete="off" placeholder="Bug Name" value="${params.name}"/>
            <button class="btn btn-secondary" type="submit" id="searchButton">Search</button>
        </g:form>
    </div>
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Person</th>
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