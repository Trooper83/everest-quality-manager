<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'testGroup.label', default: 'TestGroup')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
    <g:render template="/shared/sidebarTemplate" model="['name':testGroup.project.name, 'code':testGroup.project.code]"/>
    <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
        <g:render template="/shared/messagesTemplate" bean="${testGroup}" var="entity"/>
        <div class="hstack">
        <h1 class="text-capitalize" id="name">${testGroup.name} Details</h1>
        <sec:ifAnyGranted roles="ROLE_BASIC">
            <g:form class="ms-auto" resource="${this.testGroup}" method="DELETE" useToken="true"
                    uri="/project/${this.testGroup.project.id}/testGroup/delete/${this.testGroup.id}">
                <g:link class="btn btn-primary" uri="/project/${this.testGroup.project.id}/testGroup/edit/${this.testGroup.id}" data-test-id="show-edit-link">
                    <g:message code="default.button.edit.label" default="Edit"/>
                </g:link>
                <input class="btn btn-secondary" type="submit" data-test-id="show-delete-link"
                       value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                       onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
            </g:form>
        </sec:ifAnyGranted>
        </div>
        <table class="table table-light table-bordered mt-3">
            <thead class="thead-light">
            <tr>
                <th>Name</th>
                <th>Area</th>
                <th>Platform</th>
                <th>Type</th>
                <th>Execution Method</th>
            </tr>
            </thead>
            <tbody>
            <g:each var="test" in="${tests}">
                <tr>
                    <td><g:link uri="/project/${test.project.id}/testCase/show/${test.id}">${test.name}</g:link></td>
                    <td>${test.area?.name}</td>
                    <td>${test.platform}</td>
                    <td>${test.type}</td>
                    <td>${test.executionMethod}</td>
                </tr>
            </g:each>
            </tbody>
        </table>
        <ul class="pagination mb-5">
            <g:pagination domain="testGroup" projectId="${testGroup.project.id}" total="${testGroup.testCases.size() ?: 0}"
                    isTopLevel="false" itemId="${testGroup.id}"/>
        </ul>
    </main>
    </div>
</div>
</body>
</html>
