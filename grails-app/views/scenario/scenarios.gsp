<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'scenario.label', default: 'Scenario')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${scenario}" var="entity"/>
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:form class="mt-3" uri="/project/${project.id}/scenarios" params="['isSearch': 'true']" useToken="true">
                <div class="col-lg-4 hstack gap-3">
                    <g:textField class="form-control" name="name" autocomplete="off" placeholder="Scenario Name"
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
                            <th>Created By</th>
                            <th>Platform</th>
                            <th>Type</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each var="scenario" in="${scenarioList}">
                            <tr>
                                <td><g:link uri="/project/${project.id}/scenario/show/${scenario.id}">${scenario.name}</g:link></td>
                                <td>${scenario.description}</td>
                                <td>${scenario.person.email}</td>
                                <td>${scenario.platform}</td>
                                <td>${scenario.type}</td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <ul class="pagination mb-5">
                        <g:pagination domain="scenario" projectId="${project.id}" total="${scenarioCount ?: 0}"/>
                    </ul>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>