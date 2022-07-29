<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <a href="#edit-project" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="edit-project" class="content scaffold-edit col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${project}" var="entity"/>
            <g:form resource="${this.project}" method="PUT">
                <g:hiddenField name="version" value="${this.project?.version}" />
                <fieldset class="form">
                    <f:all bean="project" except="areas, bugs, environments, testCases, testGroups"/>
                    <div class="fieldcontain" id="areas">
                        <label>Areas</label>
                        <g:field type="text" name="area" data-toggle="tooltip" trigger="manual" title="Area Name cannot be blank"/>
                        <g:field type="button" name="btnAddArea" value="Add" onclick="addAreaTag()"/>
                        <ul class="one-to-many">
                            <g:each status="i" var="area" in="${project.areas}">
                                <li name="${area.name}">
                                    <h3>
                                        <p class="badge badge-secondary">${area.name}
                                            <svg xmlns="http://www.w3.org/2000/svg" style="cursor:pointer" width="12" height="12" fill="currentColor" class="bi bi-pencil" viewBox="0 0 20 20" onclick="displayAreaOptions(this, ${area.id})">
                                                <path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/>
                                            </svg>
                                        </p>
                                    </h3>
                                    <input style="display: none;" type="text" id="areas[${i}].name"
                                           name="areas[${i}].name" value="${area.name}" data-test-id="tag-input"
                                           data-toggle="tooltip" trigger="manual" title="Area Name cannot be blank"
                                    />
                                </li>
                            </g:each>
                        </ul>
                    </div>
                    <div class="fieldcontain" id="environments">
                        <label>Environments</label>
                        <g:field type="text" name="environment" data-toggle="tooltip" trigger="manual" title="Environment Name cannot be blank"/>
                        <g:field type="button" name="btnAddEnv" value="Add" onclick="addEnvironmentTag()"/>
                        <ul class="one-to-many">
                            <g:each status="i" var="env" in="${project.environments}">
                                <li name="${env.name}">
                                    <h3>
                                        <p class="badge badge-secondary">${env.name}
                                            <svg xmlns="http://www.w3.org/2000/svg" style="cursor:pointer" width="12" height="12" fill="currentColor" class="bi bi-pencil" viewBox="0 0 20 20" onclick="displayEnvOptions(this, ${env.id})">
                                                <path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/>
                                            </svg>
                                        </p>
                                    </h3>
                                    <input style="display: none;" type="text" id="environments[${i}].name"
                                           name="environments[${i}].name" value="${env.name}" data-test-id="tag-input"
                                           data-toggle="tooltip" trigger="manual" title="Environment Name cannot be blank"
                                    />
                                </li>
                            </g:each>
                        </ul>
                    </div>
                </fieldset>
                <fieldset class="buttons">
                    <input class="save" type="submit" data-test-id="edit-update-button" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
        <asset:javascript src="editProjectEnvironment.js"/>
        <asset:javascript src="editProjectArea.js"/>
        <asset:javascript src="popper.min.js"/>
    </body>
</html>
