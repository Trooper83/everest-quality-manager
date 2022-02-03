<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'testCycle.label', default: 'TestCycle')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-testCycle" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="create-testCycle" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${testCycle}" var="entity"/>
            <g:form resource="${this.testCycle}" method="POST">
                <fieldset class="form">
                    <g:hiddenField name="releasePlan.id" value="${releasePlan.id}" />
                    <div class="fieldcontain">
                        <label for="environ">Environment</label>
                        <g:select name="environ" from="${releasePlan.project.environments}"
                                  optionKey="id" optionValue="name"
                                  noSelection="${['':'Select an Environment...']}"
                        />
                    </div>
                    <f:all bean="testCycle" except="environ, releasePlan, testIterations"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                    <g:link elementId="cancel" class="btn btn-link" action="show" controller="releasePlan" id="${releasePlan.id}">Cancel</g:link>
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
