<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'bug.label', default: 'Bug')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
    <g:render template="/shared/projectButtonsTemplate"/>
        <div id="create-bug" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${bug}" var="entity"/>
            <g:form resource="${this.bug}" method="POST">
                <fieldset class="form">
                    <f:all bean="bug" except="area, person, environments, project, steps"/>
                    <g:hiddenField name="project" value="${project.id}"/>
                    <div class="fieldcontain">
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
                </fieldset>
                <g:render template="/shared/createStepsTableTemplate"/>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>
        <asset:javascript src="step.js"/>
        <asset:javascript src="popper.min.js"/>
    </body>
</html>
