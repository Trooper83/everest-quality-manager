<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'testGroup.label', default: 'TestGroup')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#list-testGroup" class="skip" tabindex="-1">
            <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
        </a>
        <g:render template="/shared/listPageNavigationTemplate"/>
        <div id="list-testGroup" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${testGroup}" var="entity"/>
            <f:table collection="${testGroupList}" order="name, project" />

            <div class="pagination">
                <g:paginate total="${testGroupCount ?: 0}" />
            </div>
        </div>
    </body>
</html>