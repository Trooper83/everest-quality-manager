<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'scenario.label', default: 'Scenario')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#list-scenario" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li>
                    <a class="home" href="${createLink(uri: '/')}" data-test-id="index-home-link">
                        <g:message code="default.home.label"/>
                    </a>
                </li>
                <sec:ifAnyGranted roles="ROLE_BASIC">
                    <li>
                        <g:link class="create" action="create" data-test-id="index-create-button">
                            <g:message code="default.new.label" args="[entityName]"/>
                        </g:link>
                    </li>
                </sec:ifAnyGranted>
            </ul>
        </div>
        <div id="list-scenario" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <table class="table">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Person</th>
                    <th>Project</th>
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
                        <td>${scenario.project.name}</td>
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
    </body>
</html>