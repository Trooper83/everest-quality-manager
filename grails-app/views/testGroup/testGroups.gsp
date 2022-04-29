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
        <g:render template="/shared/projectButtonsTemplate"/>
        <div id="list-testGroup" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${testGroup}" var="entity"/>
            <table class="table">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Project</th>
                </tr>
                </thead>
                <tbody>
                <g:each var="testGroup" in="${testGroupList}">
                    <tr>
                        <td><g:link uri="/project/${project.id}/testGroup/show/${testGroup.id}">${testGroup.name}</g:link></td>
                        <td>${testGroup.project.name}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${testGroupCount ?: 0}" />
            </div>
        </div>
    <asset:javascript src="popper.min.js"/>
    </body>
</html>