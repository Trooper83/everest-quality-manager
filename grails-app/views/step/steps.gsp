<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testCase.label', default: 'Step')}"/>
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${step}" var="entity"/>
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:form class="mt-3" uri="/project/${project.id}/steps" params="['isSearch': 'true']" useToken="true">
                <div class="col-lg-6 hstack gap-3">
                    <g:textField class="form-control" name="name" autocomplete="off" placeholder="Step Name"
                                 value="${params.name}"/>
                    <button class="btn btn-primary" type="submit" id="searchButton">Search</button>
                    <g:link uri="/project/${project.id}/steps" elementId="resetLink">Reset</g:link>
                </div>
            </g:form>
            <div class="row mt-3">
                <div class="col">
                    <table class="table table-light table-bordered">
                        <thead class="thead-light">
                        <tr>
                            <th>Name</th>
                            <th>Action</th>
                            <th>Result</th>
                            <th>Created By</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each var="step" in="${stepList}">
                            <tr>
                                <td><g:link uri="/project/${project.id}/step/show/${step.id}">${step.name}</g:link></td>
                                <td>${step.action}</td>
                                <td>${step.result}</td>
                                <td>${step.person.email}</td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <ul class="pagination mb-5">
                        <g:pagination domain="step" projectId="${project.id}" total="${stepCount ?: 0}"/>
                    </ul>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>