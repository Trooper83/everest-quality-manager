<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testCycle.label', default: 'TestCycle')}"/>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':testCycle.releasePlan.project.name, 'code':testCycle.releasePlan.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${testCycle}" var="entity"/>
            <div class="card mt-3">
                <div class="card-header hstack gap-1">
                    <h1 class="me-auto">
                        <g:message code="default.show.label" args="[entityName]"/>
                    </h1>
                    <sec:ifAnyGranted roles="ROLE_BASIC">
                        <button id="addTestsBtn" type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#testsModal">
                            Add Tests
                        </button>
                    </sec:ifAnyGranted>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col-4 fw-bold">Name</p>
                                <p class="col" id="name">${testCycle.name}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col-4 fw-bold">Release Plan</p>
                                <p class="col">
                                    <g:link class="property-value" elementId="releasePlan"
                                            uri="/project/${this.testCycle.releasePlan.project.id}/releasePlan/show/${testCycle.releasePlan.id}">
                                        ${testCycle.releasePlan.name}</g:link>
                                </p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col-4 fw-bold">Environment</p>
                                <p class="col" id="environment">${testCycle.environ?.name}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col-4 fw-bold">Platform</p>
                                <p class="col" id="platform">${testCycle.platform}</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="card mt-3">
                <div class="card-header hstack">
                    <h1 class="me-auto">Testing Progress</h1>
                    <g:set var="unexecuted" value="${testCycle.testIterations.count {it.lastResult == null}}" />
                    <g:set var="pass" value="${testCycle.testIterations.count {it.lastResult == 'PASSED'}}" />
                    <g:set var="fail" value="${testCycle.testIterations.count {it.lastResult == 'FAILED'}}" />
                    <g:set var="skip" value="${testCycle.testIterations.count {it.lastResult == 'SKIPPED'}}" />
                    <g:set var="total" value="${testCycle.testIterations.size()}" />
                    <g:multiProgressBar total="${total}" unexecuted="${unexecuted}" passed="${pass}"
                                        failed="${fail}" skipped="${skip}"/>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col fw-bold">UnExecuted</p>
                                <p class="col fw-bold">Passed</p>
                                <p class="col fw-bold">Failed</p>
                                <p class="col fw-bold">Skipped</p>
                                <p class="col fw-bold">Total</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col" id="unexecuted">${unexecuted}</p>
                                <p class="col" id="pass">${pass}</p>
                                <p class="col" id="fail">${fail}</p>
                                <p class="col" id="skip">${skip}</p>
                                <p class="col" id="total">${total}</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="row mt-3">
                <div class="col">
                    <table class="table table-light table-bordered">
                        <thead class="thead-light">
                        <tr>
                            <g:columnSort domain="testCycle" projectId="${testCycle.releasePlan.project.id}"
                                          property="name" title="Name" isTopLevel="false" itemId="${testCycle.id}"/>
                            <g:columnSort domain="testCycle" projectId="${testCycle.releasePlan.project.id}"
                                          property="lastResult" title="Result" isTopLevel="false" itemId="${testCycle.id}"/>
                            <g:columnSort domain="testCycle" projectId="${testCycle.releasePlan.project.id}"
                                          property="lastExecutedBy" title="Executed By" isTopLevel="false" itemId="${testCycle.id}"/>
                            <g:columnSort domain="testCycle" projectId="${testCycle.releasePlan.project.id}"
                                          property="lastExecuted" title="Date Executed" isTopLevel="false" itemId="${testCycle.id}"/>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each var="iteration" in="${iterations}">
                            <tr>
                                <td><g:link uri="/project/${testCycle.releasePlan.project.id}/testIteration/show/${iteration.id}">${iteration.name}</g:link></td>
                                <td>
                                    <g:if test="${iteration.lastResult}">
                                        <g:if test="${iteration.lastResult == 'PASSED'}">
                                            <span class="badge text-bg-success">${iteration.lastResult}</span>
                                        </g:if>
                                        <g:elseif test="${iteration.lastResult == 'FAILED'}">
                                            <span class="badge text-bg-danger">${iteration.lastResult}</span>
                                        </g:elseif>
                                        <g:else>
                                            <span class="badge text-bg-warning">${iteration.lastResult}</span>
                                        </g:else>
                                    </g:if>
                                    <g:else>
                                        <span class="badge text-bg-secondary">UNEXECUTED</span>
                                    </g:else>
                                </td>
                                <td data-name="executedBy">${iteration?.lastExecutedBy?.email}</td>
                                <td data-name="executedDateValue">${iteration?.lastExecuted}</td>
                                <td>
                                    <sec:ifAnyGranted roles="ROLE_BASIC">
                                        <g:link uri="/project/${testCycle.releasePlan.project.id}/testIteration/execute/${iteration.id}">Execute</g:link>
                                    </sec:ifAnyGranted>
                                </td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <ul class="pagination mb-5">
                        <g:pagination domain="testCycle" projectId="${testCycle.releasePlan.project.id}"
                                      total="${testCycle.testIterations.size() ?: 0}" isTopLevel="false" itemId="${testCycle.id}"/>
                    </ul>
                </div>
            </div>
        </main>
    </div>
</div>
<div class="modal fade" id="testsModal" tabindex="-1" aria-labelledby="testsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="testsModalLabel">Add Tests</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"
                        data-test-id="modal-close-button">
                </button>
            </div>
            <div class="modal-body">
                <g:form name="addTests" uri="/testCycle/addTests?testCycleId=${testCycle.id}" method="POST"
                        resource="${this.testCycle}" useToken="true">
                    <div class="row justify-content-center">
                        <div class="col-8 mb-3">
                            <label class="form-label" for="testGroups">Test Groups</label>
                            <g:select
                                    name="testGroups" from="${testGroups}" class="form-select"
                                    optionKey="id" optionValue="name" multiple="true"
                            />
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"
                                data-test-id="modal-cancel-button">Cancel
                        </button>
                        <g:submitButton data-test-id="modal-submit-button" class="btn btn-primary" name="submit" value="Add Tests"/>
                    </div>
                </g:form>
            </div>
        </div>
    </div>
</div>
<asset:javascript src="jquery-3.3.1.min.js"/>
<asset:javascript src="time.js"/>
<script>
        $(document).ready(function() {
            $('#testsModal').on('hidden.bs.modal', function () {
                $('#testsModal form')[0].reset();
            });
        });
    </script>
</body>
</html>
