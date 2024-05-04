<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testRun.label', default: 'Test Run')}"/>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <g:set var="project" value="${testRun.project}"/>
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${testRun}" var="entity"/>
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
                                <p id="name-label" class="col-4 fw-bold">Name</p>
                                <p class="col" id="name" aria-labelledby="name-label">${testRun.name}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="executed-label" class="col-4 fw-bold">Date Executed</p>
                                <p class="col" id="executed" aria-labelledby="executed-label" data-name="createdDateValue">${testRun.dateCreated}</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="card mt-3">
                <div class="card-header">
                    <h1 class="me-auto">Test Run Statistics</h1>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col fw-bold">Total</p>
                                <p class="col fw-bold">Pass %</p>
                                <p class="col fw-bold">Passed</p>
                                <p class="col fw-bold">Failed</p>
                                <p class="col fw-bold">Skipped</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col" id="total">${testRun.testResults.size()}</p>
                                <p class="col" id="passPercent">
                                    <g:if test="${testRun.testResults.size() < 1}">
                                        0.00
                                    </g:if>
                                    <g:else>
                                        ${String.format("%.2f", ((testRun.testResults.count {it.result == 'PASSED'} / testRun.testResults.size()) * 100))}
                                    </g:else>
                                </p>
                                <p class="col" id="pass">${testRun.testResults.count {it.result == 'PASSED'}}</p>
                                <p class="col" id="fail">${testRun.testResults.count {it.result == 'FAILED'}}</p>
                                <p class="col" id="skip">${testRun.testResults.count {it.result == 'SKIPPED'}}</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <h1 class="mt-3">Test Results</h1>
            <table class="table table-light table-bordered mt-3">
                <thead class="thead-light">
                <tr>
                    <g:columnSort domain="testRun" projectId="${testRun.project.id}" property="automatedTest" title="Automated Test"
                                  isTopLevel="false" itemId="${testRun.id}"/>
                    <g:columnSort domain="testRun" projectId="${testRun.project.id}" property="result" title="Result"
                                  isTopLevel="false" itemId="${testRun.id}"/>
                    <th>Link To Test</th>
                </tr>
                </thead>
                <tbody>
                <g:each status="i" var="result" in="${results}">
                    <g:if test="${result.failureCause}">
                        <tr>
                            <td class="col-8">
                                <button class="btn btn-link" type="button"
                                        data-bs-toggle="collapse"
                                        data-bs-target="#failedCause-${i}">${result.automatedTest.fullName}
                                </button>
                                <div class="collapse" id="failedCause-${i}">
                                    <div class="card">
                                        <div class="card-body bg-dark text-danger" data-test-id="failureCause">
                                            <g:failureCause value="${result.failureCause}" />
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <td class="col-2 align-middle">
                                <span class="badge text-bg-danger">${result.result}</span>
                            </td>
                            <td class="col-2 align-middle">
                                <button class="btn btn-link">
                                    <g:link uri="/project/${project.id}/automatedTest/show/${result.automatedTest.id}">
                                    <asset:image src="icons/link-45deg.svg" alt="link" width="20" height="20"/>
                                    </g:link>
                                </button>
                            </td>
                        </tr>
                    </g:if>
                    <g:else>
                        <tr>
                            <td class="col-8 align-middle">${result.automatedTest.fullName}</td>
                            <td class="col-2 align-middle">
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
                            <td class="col-2 align-middle">
                                <button class="btn btn-link">
                                    <g:link uri="/project/${project.id}/automatedTest/show/${result.automatedTest.id}">
                                        <asset:image src="icons/link-45deg.svg" alt="link" width="20" height="20"/>
                                    </g:link>
                                </button>
                            </td>
                        </tr>
                    </g:else>
                </g:each>
                </tbody>
            </table>
            <ul class="pagination mb-5">
                <g:pagination domain="testRun" projectId="${testRun.project.id}" total="${testRun.testResults.size() ?: 0}"
                          isTopLevel="false" itemId="${testRun.id}"/>
            </ul>
        </main>
    </div>
</div>
<asset:javascript src="time.js"/>
</body>
</html>
