<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'testGroup.label', default: 'TestGroup')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-testGroup" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <g:render template="/shared/createPageNavigationTemplate"/>
        <div id="create-testGroup" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${testGroup}" var="entity"/>
            <g:form resource="${this.testGroup}" method="POST">
                <fieldset class="form">
                    <div class="fieldcontain required">
                        <label for="project">Project
                            <span class="required-indicator">*</span>
                        </label>
                        <g:select name="project" from="${projects}"
                                  optionKey="id" optionValue="name"
                                  noSelection="${['':'Select a Project...']}"
                        />
                    </div>
                    <f:all bean="testGroup" except="project, testCases"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
