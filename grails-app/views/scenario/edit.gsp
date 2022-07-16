<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'scenario.label', default: 'Scenario')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <g:render template="/shared/sidebarTemplate" model="['name':scenario.project.name, 'code':scenario.project.code]"/>
        <a href="#edit-scenario" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="edit-scenario" class="content scaffold-edit col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${scenario}" var="entity"/>
            <g:form resource="${this.scenario}" method="PUT" uri="/project/${scenario.project.id}/scenario/update/${scenario.id}">
                <g:hiddenField name="version" value="${this.scenario?.version}" />
                <fieldset class="form">
                    <div class="fieldcontain">
                        <label for="area">Area</label>
                        <g:select name="area" id="area" from="${scenario.project.areas}"
                                  optionKey="id" optionValue="name" value="${scenario.area?.id}"
                                  noSelection="${['':'']}"
                        />
                    </div>
                    <div class="fieldcontain">
                        <label for="environments">Environments</label>
                        <g:select name="environments" id="environments" from="${scenario.project.environments}"
                                  optionKey="id" optionValue="name" value="${scenario.environments?.id}"
                                  noSelection="${['':'No Environment...']}" multiple="true"
                        />
                    </div>
                    <f:all bean="scenario" except="area, environments, project, person, gherkin"/>
                    <div class="fieldcontain">
                        <label for="gherkin">Gherkin</label>
                        <g:textArea name="gherkin"/>
                    </div>
                </fieldset>
                <fieldset class="buttons">
                    <input class="save" type="submit" data-test-id="edit-update-button" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
    <asset:javascript src="popper.min.js"/>
    </body>
</html>
