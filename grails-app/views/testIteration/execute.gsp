<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main"/>
        <g:set var="entityName" value="${message(code: 'testIteration.label', default: 'TestIteration')}"/>
        <title>Execute Test</title>
    </head>
    <body>
    <a href="#execute-testIteration" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

    <div id="execute-testIteration" class="content" role="main">
        <h1><g:message code="default.show.label" args="[entityName]" /></h1>
        <g:render template="/shared/messagesTemplate" bean="${testIteration}" var="entity"/>
        <g:form resource="${this.testIteration}" method="PUT" uri="/project/${testIteration.testCycle.project.id}/testIteration/update/${testIteration.id}">
            <g:hiddenField name="version" value="${this.testIteration?.version}" />
            <fieldset class="form">
                <ol class="property-list testIteration">
                    <li class="fieldcontain">
                        <span id="result-label" class="property-label">Result</span>
                        <g:select name="result" id="result" from="${['ToDo', 'Pass', 'Fail']}" value="${testIteration.result}"/>
                    </li>
                </ol>
            </fieldset>
            <sec:ifAnyGranted roles="ROLE_BASIC">
                <fieldset>
                    <input class="save" type="submit" data-test-id="edit-update-button" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </sec:ifAnyGranted>
        </g:form>
        <f:display bean="testIteration" except="steps, testCase, result, testCycle" />
        <ol class="property-list testIteration">
        <li class="fieldcontain">
            <span id="testCase-label" class="property-label">Test Case</span>
            <g:link class="property-value" elementId="testCase"
                    uri="/project/${this.testIteration.testCycle.releasePlan.project.id}/testCase/show/${testIteration.testCase.id}">${testIteration.testCase.name}</g:link>
        </li>
        <li class="fieldcontain">
            <span id="testCycle-label" class="property-label">Test Cycle</span>
            <div class="property-value" id="testCycle">${testIteration.testCycle.name}</div>
        </li>
        </ol>
        <g:render template="/shared/showStepsTableTemplate" bean="${testIteration}" var="entity"/>
    </div>
    </body>
</html>