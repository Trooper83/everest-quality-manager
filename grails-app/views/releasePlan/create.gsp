<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'releasePlan.label', default: 'ReleasePlan')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-releasePlan" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li>
                    <a class="home" href="${createLink(uri: '/')}" data-test-id="create-home-link">
                        <g:message code="default.home.label"/>
                    </a>
                </li>
                <li>
                    <g:link class="list" action="index" data-test-id="create-list-link">
                        <g:message code="default.list.label" args="[entityName]" />
                    </g:link>
                </li>
            </ul>
        </div>
        <div id="create-releasePlan" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${releasePlan}" var="entity"/>
            <g:form resource="${this.releasePlan}" method="POST">
                <fieldset class="form">
                    <div class="fieldcontain required">
                        <label for="project">Project
                            <span class="required-indicator">*</span>
                        </label>
                        <g:select name="project" from="${projects}"
                                  optionKey="id" optionValue="name"
                                  noSelection="${['':'Select a Project...']}"
                        />
                    </div>
                    <f:all bean="releasePlan" except="project, testCycles"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
