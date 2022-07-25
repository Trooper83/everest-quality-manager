<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'testIteration.label', default: 'TestIteration')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<g:render template="/shared/sidebarTemplate"
          model="['name':testIteration.testCycle.releasePlan.project.name, 'code':testIteration.testCycle.releasePlan.project.code]"/>
<a href="#show-testIteration" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div id="show-testIteration" class="content scaffold-show col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
    <g:render template="/shared/messagesTemplate" bean="${testIteration}" var="entity"/>
    <ol class="property-list testIteration">
        <li class="fieldcontain">
            <span id="name-label" class="property-label">Name</span>
            <div class="property-value" aria-labelledby="name-label">${testIteration.name}</div>
        </li>
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
        <li class="fieldcontain">
            <span id="notes-label" class="property-label">Notes</span>
            <div class="property-value" aria-labelledby="notes-label">${testIteration.notes}</div>
        </li>
        <li class="fieldcontain">
            <span id="result-label" class="property-label">Result</span>
            <div class="property-value" aria-labelledby="result-label">${testIteration.result}</div>
        </li>
    <g:link class="btn btn-secondary" role="button"
            uri="/project/${this.testIteration.testCycle.releasePlan.project.id}/testIteration/execute/${this.testIteration.id}">Execute</g:link>
    </ol>
        <g:render template="/shared/showStepsTableTemplate" bean="${testIteration}" var="entity"/>
</div>
</body>
</html>
