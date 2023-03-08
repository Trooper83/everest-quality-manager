<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'bug.label', default: 'Bug')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
<g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
<div id="create-bug" class="content scaffold-create col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
    <g:render template="/shared/messagesTemplate" bean="${bug}" var="entity"/>
    <g:form resource="${this.bug}" method="POST" uri="/project/${project.id}/bug/save" useToken="true">
        <fieldset class="form">
            <f:all bean="${this.bug}" except="actual, area, expected, person, environments, project, steps, status"/>
            <g:hiddenField name="project" value="${project.id}"/>
            <g:hiddenField name="status" value="Open"/>
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
        </fieldset>
        <g:render template="/shared/createStepsTableTemplate"/>
        <fieldset>
            <div>
                <label for="expected">Expected
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="expected"></g:textField>
                <label for="actual">Actual
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="actual"></g:textField>
            </div>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
        </fieldset>
    </g:form>
</div>
<asset:javascript src="step.js"/>
<asset:javascript src="popper.min.js"/>
</body>
</html>
