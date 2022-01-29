<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'testCycle.label', default: 'TestCycle')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-testCycle" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="create-testCycle" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.testCycle}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.testCycle}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.testCycle}" method="POST">
                <fieldset class="form">
                    <g:hiddenField name="releasePlan.id" value="${releasePlan.id}" />
                    <div class="fieldcontain required">
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
                    <g:link class="btn btn-link" action="show" controller="releasePlan" id="${releasePlan.id}">Cancel</g:link>
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
