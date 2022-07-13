<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'bug.label', default: 'Bug')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#edit-bug" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="edit-bug" class="content scaffold-edit" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${bug}" var="entity"/>
            <g:form resource="${this.bug}" method="PUT" uri="/project/${bug.project.id}/bug/update/${bug.id}">
                <g:hiddenField name="version" value="${this.bug?.version}" />
                <fieldset class="form">
                    <div class="fieldcontain">
                        <label for="area">Area</label>
                        <g:select name="area" id="area" from="${bug.project.areas}"
                                  optionKey="id" optionValue="name" value="${bug.area?.id}"
                                  noSelection="${['':'']}"
                        />
                    </div>
                    <div class="fieldcontain">
                        <label for="environments">Environments</label>
                        <g:select name="environments" id="environments" from="${bug.project.environments}"
                                  optionKey="id" optionValue="name" value="${bug.environments?.id}"
                                  noSelection="${['':'No Environment...']}" multiple="true"
                        />
                    </div>
                    <f:all bean="bug" except="area, environments, project, steps, person"/>
                </fieldset>
                <g:render template="/shared/editStepsTableTemplate" bean="${bug}" var="entity"/>
                <fieldset class="buttons">
                    <input class="save" type="submit" data-test-id="edit-update-button" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
        <asset:javascript src="step.js"/>
        <asset:javascript src="popper.min.js"/>
    </body>
</html>
