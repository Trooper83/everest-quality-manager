<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="Projects" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<main class="container">
    <div class="mt-3">
        <g:if test="${flash.message}">
            <div class="alert alert-primary" role="alert">${flash.message}</div>
        </g:if>
        <g:if test="${flash.error}">
            <ul class="alert alert-danger" role="alert">${flash.error}</ul>
        </g:if>
    </div>
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:form class="row mt-3" action="projects" controller="project" params="['isSearch': 'true']" useToken="true">
        <div class="col-lg-4 hstack gap-3">
            <g:textField class="form-control" name="name" autocomplete="off" placeholder="Project Name"
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
            <ul class="pagination mb-5">
                <g:pagination domain="project" total="${projectCount ?: 0}"/>
            </ul>
        </div>
    </div>
</main>
</body>
</html>