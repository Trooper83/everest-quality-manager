<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'testCase.label', default: 'TestCase')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <g:render template="/shared/sidebarTemplate" model="['name':testCase.project.name, 'code':testCase.project.code]"/>
        <a href="#edit-testCase" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="edit-testCase" class="content scaffold-edit col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${testCase}" var="entity"/>
            <g:form resource="${this.testCase}" method="PUT" useToken="true"
                    uri="/project/${testCase.project.id}/testCase/update/${testCase.id}">
                <g:hiddenField name="version" value="${this.testCase?.version}" />
                <fieldset class="form">
                    <div class="fieldcontain">
                        <label for="area">Area</label>
                        <g:select name="area" id="area" from="${testCase.project.areas}"
                                  optionKey="id" optionValue="name" value="${fieldValue(bean: testCase, field: 'area.id')}"
                                  noSelection="${['':'']}"
                        />
                    </div>
                    <div class="fieldcontain">
                        <label for="environments">Environments</label>
                        <g:select name="environments" id="environments" from="${testCase.project.environments}"
                                  optionKey="id" optionValue="name" value="${testCase.environments?.id}"
                                  noSelection="${['':'No Environment...']}" multiple="true"
                        />
                    </div>
                    <div class="fieldcontain">
                        <label for="testGroups">Test Groups</label>
                        <g:select name="testGroups" id="testGroups" from="${testCase.project.testGroups}"
                                  optionKey="id" optionValue="name" value="${testCase.testGroups?.id}"
                                  noSelection="${['':'No Test Group...']}" multiple="true"
                        />
                    </div>
                    <f:all bean="testCase" except="area, environments, project, steps, person, testGroups"/>
                </fieldset>
                <g:render template="/shared/editStepsTableTemplate" bean="${testCase}" var="entity"/>
                <fieldset class="buttons">
                    <input class="save" type="submit" data-test-id="edit-update-button" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
        <asset:javascript src="step.js"/>
        <asset:javascript src="popper.min.js"/>
    </body>
</html>
