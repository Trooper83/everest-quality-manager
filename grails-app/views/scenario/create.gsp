<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'scenario.label', default: 'Scenario')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-scenario" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li>
                    <a class="home" href="${createLink(uri: '/')}" data-test-id="create-home-link">
                        <g:message code="default.home.label"/>
                    </a>
                </li>
                <li>
                    <g:link class="list" action="index" data-test-id="create-list-link">
                        <g:message code="default.list.label" args="[entityName]" />
                    </g:link>
                </li>
            </ul>
        </div>
        <div id="create-scenario" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${scenario}" var="entity"/>
            <g:form resource="${this.scenario}" method="POST">
                <fieldset class="form">
                    <div class="fieldcontain required">
                        <g:hiddenField name="project" value="${project.id}"/>
                        <div class="fieldcontain required">
                            <label for="project">Project</label>
                            <span>${project.name}</span>
                        </div>
                    </div>
                    <div class="fieldcontain">
                        <label for="area">Area</label>
                        <g:select name="areas" from="${project.areas}"
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
                        <label for="gherkin">Gherkin</label>
                        <g:textArea name="gherkin"/>
                    </div>
                    <f:all bean="scenario" except="area, gherkin, environments, project, steps, person"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
