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
            <f:table collection="${scenarioList}"
                     order="['name', 'person', 'type', 'executionMethod', 'platform', 'project']"/>

            <div class="pagination">
                <g:paginate total="${scenarioCount ?: 0}" />
            </div>
        </div>
    </body>
</html>