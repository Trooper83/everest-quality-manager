<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'releasePlan.label', default: 'ReleasePlan')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-releasePlan" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="create-releasePlan" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${releasePlan}" var="entity"/>
            <g:form resource="${this.releasePlan}" method="POST" uri="/project/${project.id}/releasePlan/save">
                <fieldset class="form">
                    <g:hiddenField name="project" value="${project.id}"/>
                    <f:all bean="releasePlan" except="project, testCycles"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>
    <asset:javascript src="popper.min.js"/>
    </body>
</html>
