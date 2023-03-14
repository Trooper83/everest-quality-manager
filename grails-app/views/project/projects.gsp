<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="container height-100" id="list-project" role="main">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
        <div class="alert alert-primary" role="alert">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
        <ul class="alert alert-danger" role="alert">${flash.error}</ul>
    </g:if>
    <div class="row">
        <div class="col-6 mt-3">
            <g:form action="projects" controller="project" params="['isSearch': 'true']" useToken="true">
                <div class="form-row">
                    <div class="col">
                        <g:textField class="form-control" name="name" autocomplete="off" placeholder="Project Name"
                                 value="${params.name}"/>
                    </div>
                    <div class="col">
                        <button class="btn btn-primary" type="submit" id="searchButton">Search</button>
                    </div>
                </div>
            </g:form>
        </div>
    </div>
    <div class="row mt-3">
        <div class="col">
            <table class="table table-hover table-striped table-sm table-bordered">
                <thead class="thead-light">
                <tr>
                    <th>Name</th>
                    <th>Code</th>
                </tr>
                </thead>
                <tbody>
                <g:each var="project" in="${projectList}">
                    <tr>
                        <td><g:link uri="/project/home/${project.id}">${project.name}</g:link></td>
                        <td>${project.code}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${projectCount ?: 0}" />
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>