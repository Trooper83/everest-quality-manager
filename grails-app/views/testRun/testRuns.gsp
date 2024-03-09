<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testRun.label', default: 'Test Run')}"/>
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${testRun}" var="entity"/>
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:form class="mt-3" uri="/project/${project.id}/testRuns" params="['isSearch': 'true']" useToken="true">
                <div class="col-lg-4 hstack gap-3">
                    <g:textField class="form-control" name="searchTerm" autocomplete="off" placeholder="Test Run Name"
                                 value="${params.searchTerm}"/>
                    <button class="btn btn-primary" type="submit" id="searchButton">Search</button>
                    <g:link uri="/project/${project.id}/testRuns" elementId="resetLink">Reset</g:link>
                </div>
            </g:form>
            <div class="row mt-3">
                <div class="col">
                    <table class="table table-light table-bordered">
                        <thead class="thead-light">
                        <tr>
                            <g:columnSort domain="testRun" projectId="${project.id}" property="name" title="Name"/>
                            <g:columnSort domain="testRun" projectId="${project.id}" property="dateCreated" title="Created"/>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each var="run" in="${testRunList}">
                            <tr>
                                <td><g:link uri="/project/${project.id}/testRun/show/${run.id}">${run.name}</g:link></td>
                                <td data-name="createdDateValue">${run.dateCreated}</td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <ul class="pagination mb-5">
                        <g:pagination domain="testRun" projectId="${project.id}" total="${testRunCount ?: 0}"/>
                    </ul>
                </div>
            </div>
        </main>
    </div>
</div>
<asset:javascript src="time.js"/>
</body>
</html>