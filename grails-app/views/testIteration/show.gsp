<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testIteration.label', default: 'TestIteration')}"/>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
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
                    <sec:ifAnyGranted roles="ROLE_BASIC">
                        <g:link class="btn btn-primary" role="button"
                                uri="/project/${this.testIteration.testCycle.releasePlan.project.id}/testIteration/execute/${this.testIteration.id}">
                            Execute
                        </g:link>
                    </sec:ifAnyGranted>
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
            <div class="card mt-3 mb-5">
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
            <h1 class="mt-3">Results</h1>
            <table class="table table-light table-bordered mt-3">
                <thead class="thead-light">
                <tr>
                    <th>#</th>
                    <th>Result</th>
                    <th>Date Executed</th>
                    <th>Executed By</th>
                    <th>Notes</th>
                </tr>
                </thead>
                <tbody>
                <g:each status="i" var="result" in="${testIteration.results.reverse()}">
                    <tr>
                        <td>${testIteration.results.size() - i}</td>
                        <td>
                            <g:if test="${result.result == 'PASSED'}">
                                <span class="badge text-bg-success">${result.result}</span>
                            </g:if>
                            <g:elseif test="${result.result == 'FAILED'}">
                                <span class="badge text-bg-danger">${result.result}</span>
                            </g:elseif>
                            <g:else>
                                <span class="badge text-bg-warning">${result.result}</span>
                            </g:else>
                        </td>
                        <td data-name="createdDateValue">${result.dateCreated}</td>
                        <td data-name="executedByValue">${result.person.email}</td>
                        <td class="text-break">${result.notes}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </main>
    </div>
</div>
<asset:javascript src="time.js"/>
</body>
</html>
