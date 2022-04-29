<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: "testCase.label", default: "TestCase")}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-testCase" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <g:render template="/shared/projectButtonsTemplate"/>
        <div id="create-testCase" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${testCase}" var="entity"/>
            <g:form controller="testCase" action="save" method="POST">
                <fieldset class="form">
                    <g:hiddenField name="project" value="${project.id}"/>
                    <div class="fieldcontain required">
                        <label for="project">Project</label>
                        <span>${project.name}</span>
                    </div>
                    <div class="fieldcontain">
                        <label for="area">Area</label>
                        <g:select name="area" from="${project.areas}"
                                  optionKey="id" optionValue="name"
                                  noSelection="${['':'Select an Area...']}"
                        />
                    </div>
                    <div class="fieldcontain">
                        <label for="environments">Environments</label>
                        <g:select name="environments" from="${project.environments}"
                                  optionKey="id" optionValue="name"
                                  noSelection="${['':'Select Environments...']}"
                                  multiple="true"
                        />
                    </div>
                    <div class="fieldcontain">
                        <label for="testGroups">Test Groups</label>
                        <g:select name="testGroups" from="${project.testGroups}"
                                  optionKey="id" optionValue="name"
                                  noSelection="${['':'Select Test Groups...']}"
                                  multiple="true"
                        />
                    </div>
                    <f:all bean="testCase" except="area, environments, project, steps, person, testGroups"/>
                </fieldset>
                <g:render template="/shared/createStepsTableTemplate"/>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: "default.button.create.label", default: "Create")}" />
                </fieldset>
            </g:form>
        </div>
        <asset:javascript src="step.js"/>
        <asset:javascript src="popper.min.js"/>
    </body>
</html>
