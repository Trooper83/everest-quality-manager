<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testIteration.label', default: 'TestIteration')}"/>
    <title>Execute Test</title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate"
                  model="['name':testIteration.testCycle.releasePlan.project.name, 'code':testIteration.testCycle.releasePlan.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${testIteration}" var="entity"/>
            <div class="card mt-3">
                <div class="card-header hstack gap-1">
                    <h1 class="me-auto">
                        <g:message code="default.show.label" args="[entityName]"/>
                    </h1>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col-4 fw-bold">Name</p>
                                <p class="col" id="name">${testIteration.name}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col-4 fw-bold">Test Case</p>
                                <p class="col">
                                    <g:link class="property-value" elementId="testCase"
                                            uri="/project/${this.testIteration.testCycle.releasePlan.project.id}/testCase/show/${testIteration.testCase.id}">
                                        ${testIteration.testCase.name}
                                    </g:link>
                                </p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col-4 fw-bold">Test Cycle</p>
                                <p class="col">
                                    <g:link class="property-value" elementId="testCycle"
                                            uri="/project/${this.testIteration.testCycle.releasePlan.project.id}/testCycle/show/${testIteration.testCycle.id}">
                                        ${testIteration.testCycle.name}
                                    </g:link>
                                </p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="card mt-3">
                <div class="card-header">
                    <h1 class="me-auto">Steps</h1>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col-4 fw-bold">Action</p>
                                <p class="col-4 fw-bold">Data</p>
                                <p class="col-4 fw-bold">Result</p>
                            </div>
                        </li>
                    </ul>
                    <g:render template="/shared/showStepsTableTemplate" bean="${testIteration}" var="entity"/>
                    <ul class="list-group list-group-flush mt-3">
                        <li class="list-group-item border-bottom mt-4">
                            <div class="row">
                                <p class="col-4 fw-bold">Verify</p>
                            </div>
                        </li>
                    </ul>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item">
                            <div class="row">
                                <p id="verify" class="col-4">${testIteration.verify}</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <g:form resource="${this.testIteration}" method="PUT" useToken="true"
                    uri="/project/${testIteration.testCycle.releasePlan.project.id}/testIteration/update/${testIteration.id}">
                <g:hiddenField name="version" value="${this.testIteration?.version}"/>
                <div class="card mt-3 mb-5">
                    <div class="card-header">
                        <h1 class="me-auto">Execution Details</h1>
                    </div>
                    <div class="card-body">
                        <div class="col-4 mb-3">
                            <label class="form-label" for="result">Result</label>
                            <g:select class="form-select" name="result" id="result" from="${['ToDo', 'Passed', 'Failed']}"
                                      value="${testIteration.result}"/>
                        </div>
                        <div class="col-6 mb-3">
                            <label class="form-label" for="notes">Notes</label>
                            <g:textArea class="form-control" name="notes" value="${testIteration.notes}"
                                        maxLength="500"></g:textArea>
                        </div>
                    </div>
                    <div class="card-footer">
                        <sec:ifAnyGranted roles="ROLE_BASIC">
                            <g:submitButton name="complete" class="btn btn-primary" value="Complete"/>
                        </sec:ifAnyGranted>
                    </div>
                </div>
            </g:form>
        </main>
    </div>
</div>
</div>
</body>
</html>