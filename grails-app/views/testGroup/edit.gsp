<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'testGroup.label', default: 'TestGroup')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <g:render template="/shared/sidebarTemplate" model="['name':testGroup.project.name, 'code':testGroup.project.code]"/>
        <a href="#edit-testGroup" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="edit-testGroup" class="content scaffold-edit col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${testGroup}" var="entity"/>
            <g:form resource="${this.testGroup}" method="PUT" uri="/project/${testGroup.project.id}/testGroup/update/${testGroup.id}">
                <g:hiddenField name="version" value="${this.testGroup?.version}" />
                <fieldset class="form">
                    <f:all bean="testGroup" except="project, testCases"/>
                </fieldset>
                <fieldset class="buttons">
                    <input class="save" type="submit" data-test-id="edit-update-button" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
    <asset:javascript src="popper.min.js"/>
    </body>
</html>
