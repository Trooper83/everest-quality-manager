<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'bug.label', default: 'Bug')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
        <asset:javascript src="application.js"/>
    </head>
    <body>
        <a href="#create-bug" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li>
                    <a class="home" data-test-id="create-home-link" href="${createLink(uri: '/')}">
                        <g:message code="default.home.label"/>
                    </a>
                </li>
                <li>
                    <g:link class="list" action="index" data-test-id="create-list-link">
                    <g:message code="default.list.label" args="[entityName]" /></g:link>
                </li>
            </ul>
        </div>
        <div id="create-bug" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${bug}" var="entity"/>
            <g:form resource="${this.bug}" method="POST">
                <fieldset class="form">
                    <input type="hidden" name="creator" value="<sec:username/>"/>
                    <f:all bean="bug" except="area, creator, project, steps"/>
                    <div class="fieldcontain required">
                        <label for="project">Project
                            <span class="required-indicator">*</span>
                        </label>
                        <g:select name="project" from="${projects}"
                              optionKey="id" optionValue="name"
                              noSelection="${['':'Select a Project...']}"
                              onchange="getAreas()"
                        />
                    </div>
                    <div class="fieldcontain">
                        <label for="area">Area</label>
                        <select name="area" id="area" disabled>
                            <option value=''>Select an Area...</option>
                        </select>
                    </div>
                </fieldset>
                <g:render template="/shared/createStepsTableTemplate"/>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>
        <asset:javascript src="step.js"/>
        <asset:javascript src="getProjectAreas.js"/>
    </body>
</html>
