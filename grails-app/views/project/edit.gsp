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
                                    <input type="button" value="x" data-test-id="remove-tag-button" onclick="removeAreaTag(this, ${area.id})"/>
                                    <input type="button" value="y" data-test-id="edit-tag-button" onclick="editTag(this)"/>
                                    <span>${area.name}</span>
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
                                    <input type="button" value="x" data-test-id="remove-tag-button" onclick="removeEnvironmentTag(this, ${env.id})"/>
                                    <input type="button" value="y" data-test-id="edit-tag-button" onclick="editTag(this)"/>
                                    <span>${env.name}</span>
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
        <asset:javascript src="project.js"/>
        <asset:javascript src="popper.min.js"/>
    </body>
</html>
