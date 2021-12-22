<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'releasePlan.label', default: 'ReleasePlan')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#list-releasePlan" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li>
                    <a class="home" href="${createLink(uri: '/')}" data-test-id="index-home-link">
                        <g:message code="default.home.label"/>
                    </a>
                </li>
                <sec:ifAnyGranted roles="ROLE_BASIC">
                <li>
                    <g:link class="create" action="create" data-test-id="index-create-link">
                        <g:message code="default.new.label" args="[entityName]" />
                    </g:link
                ></li>
                </sec:ifAnyGranted>
            </ul>
        </div>
        <div id="list-releasePlan" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <f:table collection="${releasePlanList}" />

            <div class="pagination">
                <g:paginate total="${releasePlanCount ?: 0}" />
            </div>
        </div>
    </body>
</html>