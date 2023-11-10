<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'bug.label', default: 'Bug')}"/>
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${bug}" var="entity"/>
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:form class="mt-3" uri="/project/${project.id}/bugs" params="['isSearch': 'true']" useToken="true">
                <div class="col-lg-4 hstack gap-3">
                    <g:textField class="form-control" name="name" autocomplete="off" placeholder="Bug Name"
                                 value="${params.name}"/>
                    <button class="btn btn-primary" type="submit" id="searchButton">Search</button>
                </div>
            </g:form>
            <div class="row mt-3">
                <div class="col">
                    <table class="table table-light table-bordered">
                        <thead class="thead-light">
                        <tr>
                            <g:columnSort domain="bug" projectId="${project.id}" property="name" title="Name"/>
                            <g:columnSort domain="bug" projectId="${project.id}" property="person" title="Created By"/>
                            <g:columnSort domain="bug" projectId="${project.id}" property="platform" title="Platform"/>
                            <g:columnSort domain="bug" projectId="${project.id}" property="area" title="Area"/>
                            <g:columnSort domain="bug" projectId="${project.id}" property="status" title="Status"/>
                            <g:columnSort domain="bug" projectId="${project.id}" property="dateCreated" title="Created"/>
                            <g:columnSort domain="bug" projectId="${project.id}" property="lastUpdated" title="Updated"/>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each var="bug" in="${bugList}">
                            <tr>
                                <td><g:link uri="/project/${project.id}/bug/show/${bug.id}">${bug.name}</g:link></td>
                                <td>${bug.person.email}</td>
                                <td>${bug.platform}</td>
                                <td>${bug.area?.name}</td>
                                <td>${bug.status}</td>
                                <td><g:formatDate format="MMMM d, yyyy" date="${bug.dateCreated}"/></td>
                                <td><g:formatDate format="MMMM d, yyyy" date="${bug.lastUpdated}"/></td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <ul class="pagination mb-5">
                        <g:pagination domain="bug" projectId="${project.id}" total="${bugCount ?: 0}"/>
                    </ul>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>