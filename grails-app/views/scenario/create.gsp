<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'scenario.label', default: 'Scenario')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
    <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <a href="#create-scenario" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="create-scenario" class="content scaffold-create col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${scenario}" var="entity"/>
            <g:form resource="${this.scenario}" method="POST" uri="/project/${project.id}/scenario/save">
                <fieldset class="form">
                    <g:hiddenField name="project" value="${project.id}"/>
                    <div class="fieldcontain">
                        <label for="area">Area</label>
                        <g:select name="area" from="${project.areas}"
                                  optionKey="id" optionValue="name"
                                  noSelection="${['':'']}"
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
    <asset:javascript src="popper.min.js"/>
    </body>
</html>
