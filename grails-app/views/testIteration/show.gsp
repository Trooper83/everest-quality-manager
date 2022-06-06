<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'testIteration.label', default: 'TestIteration')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#show-testIteration" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

        <div id="show-testIteration" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${testIteration}" var="entity"/>
            <ol class="property-list testIteration">
                <g:link uri="/project/${this.testIteration.testCycle.releasePlan.project.id}/testIteration/execute/${this.testIteration.id}">Execute</g:link>
                <li class="fieldcontain">
                    <span id="testCase-label" class="property-label">Test Case</span>
                    <g:link class="property-value" elementId="testCase"
                            uri="/project/${this.testIteration.testCycle.releasePlan.project.id}/testCase/show/${testIteration.testCase.id}">
                        ${testIteration.testCase.name}</g:link>
                </li>
                <li class="fieldcontain">
                    <span id="testCycle-label" class="property-label">Test Cycle</span>
                    <g:link class="property-value" elementId="testCycle"
                            uri="/project/${this.testIteration.testCycle.releasePlan.project.id}/testCycle/show/${testIteration.testCycle.id}">
                        ${testIteration.testCycle.name}</g:link>
                </li>
            </ol>
            <f:display bean="testIteration" except="steps, testCase, testCycle" />
            <g:render template="/shared/showStepsTableTemplate" bean="${testIteration}" var="entity"/>
        </div>
    </body>
</html>
