<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'automatedTest.label', default: 'Automated Test')}"/>
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${automatedTest}" var="entity"/>
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:form class="mt-3" uri="/project/${project.id}/automatedTests" params="['isSearch': 'true']" useToken="true">
                <div class="col-lg-4 hstack gap-3">
                    <g:textField class="form-control" name="fullName" autocomplete="off" placeholder="Automated Test Name"
                                 value="${params.fullName}"/>
                    <button class="btn btn-primary" type="submit" id="searchButton">Search</button>
                    <g:link uri="/project/${project.id}/automatedTests" elementId="resetLink">Reset</g:link>
                </div>
            </g:form>
            <div class="row mt-3">
                <div class="col">
                    <table class="table table-light table-bordered">
                        <thead class="thead-light">
                        <tr>
                            <g:columnSort domain="automatedTest" projectId="${project.id}" property="fullName" title="Full Name"/>
                            <g:columnSort domain="automatedTest" projectId="${project.id}" property="name" title="Name"/>
                            <g:columnSort domain="automatedTest" projectId="${project.id}" property="dateCreated" title="Created"/>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each var="test" in="${automatedTestList}">
                            <tr>
                                <td><g:link uri="/project/${project.id}/automatedTest/show/${test.id}">${test.fullName}</g:link></td>
                                <td>${test.name}</td>
                                <td data-name="createdDateValue">${test.dateCreated}</td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <ul class="pagination mb-5">
                        <g:pagination domain="automatedTest" projectId="${project.id}" total="${automatedTestCount ?: 0}"/>
                    </ul>
                </div>
            </div>
        </main>
    </div>
</div>
<asset:javascript src="time.js"/>
</body>
</html>