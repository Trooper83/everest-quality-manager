<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'releasePlan.label', default: 'ReleasePlan')}"/>
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${releasePlan}" var="entity"/>
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:form class="mt-3" uri="/project/${project.id}/releasePlans" params="['isSearch': 'true']" useToken="true">
                <div class="col-lg-4 hstack gap-3">
                    <g:textField class="form-control" name="name" autocomplete="off" placeholder="Release Plan Name"
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
                            <th>Status</th>
                            <th>Planned Date</th>
                            <th>Release Date</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each var="releasePlan" in="${releasePlanList}">
                            <tr>
                                <td>
                                    <g:link uri="/project/${project.id}/releasePlan/show/${releasePlan.id}">${releasePlan.name}</g:link>
                                </td>
                                <td>${releasePlan.status}</td>
                                <td>
                                    <g:formatDate format="MMMM d, yyyy" date="${releasePlan.plannedDate}"/>
                                </td>
                                <td>
                                    <g:formatDate format="MMMM d, yyyy" date="${releasePlan.releaseDate}"/>
                                </td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <ul class="pagination mb-5">
                        <g:pagination domain="releasePlan" projectId="${project.id}" total="${releasePlanCount ?: 0}"/>
                    </ul>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>