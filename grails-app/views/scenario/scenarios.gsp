<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'scenario.label', default: 'Scenario')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <a href="#list-scenario" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <g:render template="/shared/projectButtonsTemplate"/>
        <div id="list-scenario" class="content scaffold-list col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div class="col-4 mt-4 mb-4">
                <g:form uri="/project/${project.id}/scenarios" params="['isSearch': 'true']">
                    <g:textField name="name" autocomplete="off" placeholder="Scenario Name" />
                    <button class="btn btn-primary" type="submit" id="searchButton">Search</button>
                </g:form>
            </div>
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Person</th>
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
            <div class="pagination">
                <g:paginate total="${scenarioCount ?: 0}" />
            </div>
        </div>
    <asset:javascript src="popper.min.js"/>
    </body>
</html>