<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testIteration.label', default: 'TestIteration')}"/>
    <title>Execute Test</title>
</head>
<body>
<g:render template="/shared/sidebarTemplate"
          model="['name':testIteration.testCycle.releasePlan.project.name, 'code':testIteration.testCycle.releasePlan.project.code]"/>
<a href="#execute-testIteration" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div id="execute-testIteration" class="content col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
    <h1><g:message code="default.execute.label" args="[entityName]" /></h1>
    <g:render template="/shared/messagesTemplate" bean="${testIteration}" var="entity"/>
    <g:form resource="${this.testIteration}" method="PUT" uri="/project/${testIteration.testCycle.releasePlan.project.id}/testIteration/update/${testIteration.id}">
        <g:hiddenField name="version" value="${this.testIteration?.version}" />
        <ol class="property-list testIteration">
            <li class="fieldcontain">
                <span id="name-label" class="property-label">Name</span>
                <div class="property-value" aria-labelledby="name-label">${testIteration.name}</div>
            </li>
            <li class="fieldcontain">
                <span id="testCycle-label" class="property-label">Test Cycle</span>
                <g:link class="property-value" elementId="testCycle"
                        uri="/project/${testIteration.testCycle.releasePlan.project.id}/testCycle/show/${testIteration.testCycle.id}">
                    ${testIteration.testCycle.name}</g:link>
            </li>
            <li class="fieldcontain">
                <div id="result-label" class="property-label">Result</div>
                <g:select class="property-value" name="result" id="result" from="${['ToDo', 'Pass', 'Fail']}" value="${testIteration.result}"/>
            </li>
            <li class="fieldcontain">
                <span id="span-label" class="property-label">Notes</span>
                <g:textArea class="property-value" name="notes" value="${this.testIteration.notes}"></g:textArea>
            </li>
            <sec:ifAnyGranted roles="ROLE_BASIC">
                <input data-test-id="edit-update-button" class="btn btn-secondary" type="submit" value="Complete">
            </sec:ifAnyGranted>
        </ol>
    </g:form>
    <g:render template="/shared/showStepsTableTemplate" bean="${testIteration}" var="entity"/>
</div>
</body>
</html>