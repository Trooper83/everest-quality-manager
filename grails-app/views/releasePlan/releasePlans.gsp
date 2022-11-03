<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'releasePlan.label', default: 'ReleasePlan')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <a href="#list-releasePlan" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="row justify-content-end mt-3">
        <sec:ifAnyGranted roles="ROLE_BASIC">
            <g:if test="${params.projectId}">
                <g:link role="button" class="btn btn-secondary" elementId="createButton" uri="/project/${params.projectId}/releasePlan/create">Create Plan</g:link>
            </g:if>
        </sec:ifAnyGranted>
    </div>
        <div id="list-releasePlan" class="content scaffold-list col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div class="col-4 mt-4 mb-4">
                <g:form uri="/project/${project.id}/releasePlans" params="['isSearch': 'true']" useToken="true">
                    <g:textField name="name" autocomplete="off" placeholder="Release Plan Name" value="${params.name}"/>
                    <button class="btn btn-secondary" type="submit" id="searchButton">Search</button>
                </g:form>
            </div>
            <table>
                <thead>
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
                        <td><g:link uri="/project/${project.id}/releasePlan/show/${releasePlan.id}">${releasePlan.name}</g:link></td>
                        <td>${releasePlan.status}</td>
                        <td><g:formatDate format="MMMM d, yyyy" date="${releasePlan.plannedDate}"/></td>
                        <td><g:formatDate format="MMMM d, yyyy" date="${releasePlan.releaseDate}"/></td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${releasePlanCount ?: 0}" />
            </div>
        </div>
        <asset:javascript src="popper.min.js"/>
    </body>
</html>