<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testCase.label', default: 'TestCase')}"/>
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${testCase}" var="entity"/>
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:form class="mt-3" uri="/project/${project.id}/testCases" params="['isSearch': 'true']" useToken="true">
                <div class="col-lg-4 hstack gap-3">
                    <g:textField class="form-control" name="name" autocomplete="off" placeholder="Test Name"
                                 value="${params.name}"/>
                    <button class="btn btn-primary" type="submit" id="searchButton">Search</button>
                </div>
            </g:form>
            <div class="row mt-3">
                <div class="col">
                    <table class="table table-light table-bordered">
                        <thead class="thead-light">
                        <tr>
                            <g:columnSort domain="testCase" projectId="${project.id}" property="name" title="Name"/>
                            <g:columnSort domain="testCase" projectId="${project.id}" property="person" title="Created By"/>
                            <g:columnSort domain="testCase" projectId="${project.id}" property="area" title="Area"/>
                            <g:columnSort domain="testCase" projectId="${project.id}" property="platform" title="Platform"/>
                            <g:columnSort domain="testCase" projectId="${project.id}" property="type" title="Type"/>
                            <g:columnSort domain="testCase" projectId="${project.id}" property="executionMethod" title="Execution Method"/>
                            <g:columnSort domain="testCase" projectId="${project.id}" property="dateCreated" title="Created"/>
                            <g:columnSort domain="testCase" projectId="${project.id}" property="lastUpdated" title="Updated"/>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each var="test" in="${testCaseList}">
                            <tr>
                                <td><g:link uri="/project/${project.id}/testCase/show/${test.id}">${test.name}</g:link></td>
                                <td>${test.person.email}</td>
                                <td>${test.area?.name}</td>
                                <td>${test.platform}</td>
                                <td>${test.type}</td>
                                <td>${test.executionMethod}</td>
                                <td data-name="createdDateValue">${test.dateCreated}</td>
                                <td data-name="updatedDateValue">${test.lastUpdated}</td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <ul class="pagination mb-5">
                        <g:pagination domain="testCase" projectId="${project.id}" total="${testCaseCount ?: 0}"/>
                    </ul>
                </div>
            </div>
        </main>
    </div>
</div>
<asset:javascript src="time.js"/>
</body>
</html>