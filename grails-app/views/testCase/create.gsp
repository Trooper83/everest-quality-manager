<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: "testCase.label", default: "TestCase")}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-testCase" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: "/")}" data-test-id="create-home-link"><g:message code="default.home.label"/></a></li>
                <li>
                    <g:link class="list" action="index" data-test-id="create-list-link"><g:message code="default.list.label" args="[entityName]" /></g:link>
                </li>
            </ul>
        </div>
        <div id="create-testCase" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${testCase}" var="entity"/>
            <g:form controller="testCase" action="save" method="POST">
                <fieldset class="form">
                    <input type="hidden" name="creator" value="<sec:username/>"/>
                    <div class="fieldcontain required">
                        <label for="project">Project
                            <span class="required-indicator">*</span>
                        </label>
                        <g:select name="project" from="${projects}"
                                  optionKey="id" optionValue="name"
                                  noSelection="${['':'Select a Project...']}"
                                  onchange="getProjectItems()"
                        />
                    </div>
                    <div class="fieldcontain">
                        <label for="area">Area</label>
                        <select name="area" id="area" disabled>
                            <option value=''>Select an Area...</option>
                        </select>
                    </div>
                    <div class="fieldcontain">
                        <label for="environments">Environments</label>
                        <select name="environments" id="environments" disabled multiple>
                            <option value=''>--No Environment--</option>
                        </select>
                    </div>
                    <f:all bean="testCase" except="area, environments, project, bugs, steps, creator"/>
                </fieldset>
                <g:render template="/shared/createStepsTableTemplate"/>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: "default.button.create.label", default: "Create")}" />
                </fieldset>
            </g:form>
        </div>
        <asset:javascript src="getProjectItems.js"/>
        <asset:javascript src="step.js"/>
    </body>
</html>
