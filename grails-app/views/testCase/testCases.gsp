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
                    <ul class="pagination mb-5">
                        <g:pagination domain="testCase" projectId="${project.id}" total="${testCaseCount ?: 0}"/>
                    </ul>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>