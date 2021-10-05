<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'bug.label', default: 'Bug')}"/>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<a href="#show-bug" class="skip" tabindex="-1">
    <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
</a>
<div class="nav" role="navigation">
    <ul>
        <li>
            <a class="home" href="${createLink(uri: '/')}" data-test-id="show-home-link">
                <g:message code="default.home.label"/>
            </a>
        </li>
        <li>
            <g:link class="list" action="index" data-test-id="show-list-link">
                <g:message code="default.list.label" args="[entityName]"/>
            </g:link>
        </li>
        <sec:ifAnyGranted roles="ROLE_BASIC">
            <li>
                <g:link class="create" action="create" data-test-id="show-create-link">
                    <g:message code="default.new.label" args="[entityName]"/>
                </g:link>
            </li>
        </sec:ifAnyGranted>
    </ul>
</div>
<div id="show-bug" class="content scaffold-show" role="main">
    <h1>
        <g:message code="default.show.label" args="[entityName]"/>
    </h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <f:display bean="bug" except="steps"/>
    <fieldset>
        <table class="table">
            <thead>
            <tr>
                <th>Action</th>
                <th>Result</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${bug.steps}">
                <tr>
                    <td>${it.action}</td>
                    <td>${it.result}</td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </fieldset>
    <sec:ifAnyGranted roles="ROLE_BASIC">
        <g:form resource="${this.bug}" method="DELETE">
            <fieldset class="buttons">
                <g:link class="edit" action="edit" resource="${this.bug}" data-test-id="show-edit-link">
                    <g:message code="default.button.edit.label" default="Edit"/>
                </g:link>
                <input class="delete" type="submit" data-test-id="show-delete-link"
                       value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                       onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
            </fieldset>
        </g:form>
    </sec:ifAnyGranted>
</div>
</body>
</html>
