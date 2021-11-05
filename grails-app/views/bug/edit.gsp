<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'bug.label', default: 'Bug')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#edit-bug" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li>
                    <a class="home" data-test-id="edit-home-link" href="${createLink(uri: '/')}">
                        <g:message code="default.home.label"/>
                    </a>
                </li>
                <li>
                    <g:link class="list" action="index" data-test-id="edit-list-link">
                        <g:message code="default.list.label" args="[entityName]" />
                    </g:link>
                </li>
            </ul>
        </div>
        <div id="edit-bug" class="content scaffold-edit" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.bug}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.bug}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.bug}" method="PUT">
                <g:hiddenField name="version" value="${this.bug?.version}" />
                <fieldset class="form">
                    <div class="fieldcontain">
                        <label for="project">Project</label>
                        <span data-test-id="edit-project-name">${bug.project.name}</span>
                    </div>
                    <div class="fieldcontain">
                        <label for="area">Area</label>
                        <g:select name="area" id="area" from="${bug.project.areas}"
                                  optionKey="id" optionValue="name" value="${bug.area?.id}"
                                  noSelection="${['':'']}"
                        />
                    </div>
                    <f:all bean="bug" except="area, project, steps, creator"/>
                </fieldset>
                <fieldset>
                    <table class="table">
                        <thead>
                        <tr>
                            <th></th>
                            <th>Action</th>
                            <th>Result</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody id="stepsTableContent">
                        <g:each status="i" var="step" in="${bug.steps}">
                            <tr>
                                <td><g:hiddenField name="stepsIndex[${i}]" class="iHidden" /></td>
                                <td><g:field type="text" name="steps[${i}].action" value="${step.action}" /></td>
                                <td><g:field type="text" name="steps[${i}].result" value="${step.result}" /></td>
                                <td><input type="button" value="Remove" onclick="removeEntryRow(this, ${step.id})" /></td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <input id="btnAddRow" type="button" value="Add" onclick="addEntryRow()" />
                </fieldset>
                <fieldset class="buttons">
                    <input class="save" type="submit" data-test-id="edit-update-button" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
        <asset:javascript src="step.js"/>
    </body>
</html>
