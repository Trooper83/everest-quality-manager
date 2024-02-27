<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'automatedTest.label', default: 'Automated Test')}"/>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <g:set var="project" value="${automatedTest.project}"/>
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':automatedTest.project.name, 'code':automatedTest.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${automatedTest}" var="entity"/>
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
                                <p id="fullName-label" class="col-4 fw-bold">Full Name</p>
                                <p class="col" id="fullName" aria-labelledby="fullName-label">${automatedTest.fullName}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="name-label" class="col-4 fw-bold">Name</p>
                                <p class="col" id="name" aria-labelledby="name-label">${automatedTest.name}</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="card mt-3">
                <div class="card-header">
                    <h1 class="me-auto">Statistics</h1>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col"></p>
                                <p class="col fw-bold">Total</p>
                                <p class="col fw-bold">Pass %</p>
                                <p class="col fw-bold">Passed</p>
                                <p class="col fw-bold">Failed</p>
                                <p class="col fw-bold">Skipped</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col fw-bold">All Time</p>
                                <p class="col" id="allTimeTotal">${resultModel.total}</p>
                                <p class="col" id="allTimePassPercent">
                                    <g:if test="${resultModel.total < 1}">
                                        0.00
                                    </g:if>
                                    <g:else>
                                        ${String.format("%.2f", ((resultModel.passTotal / resultModel.total) * 100))}
                                    </g:else>
                                </p>
                                <p class="col" id="allTimePass">${resultModel.passTotal}</p>
                                <p class="col" id="allTimeFail">${resultModel.failTotal}</p>
                                <p class="col" id="allTimeSkip">${resultModel.skipTotal}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col fw-bold">Last 20</p>
                                <p class="col" id="recentTotal">${resultModel.recentTotal}</p>
                                <p class="col" id="recentPassPercent">
                                    <g:if test="${resultModel.total < 1}">
                                        0.00
                                    </g:if>
                                    <g:else>
                                        ${String.format("%.2f", ((resultModel.recentPassTotal / resultModel.recentTotal) * 100))}
                                    </g:else>
                                </p>
                                <p class="col" id="recentPass">${resultModel.recentPassTotal}</p>
                                <p class="col" id="recentFail">${resultModel.recentFailTotal}</p>
                                <p class="col" id="recentSkip">${resultModel.recentSkipTotal}</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <h1 class="mt-3">Recent Results</h1>
            <table class="table table-light table-bordered mt-3">
                <thead class="thead-light">
                <tr>
                    <th>Test Run</th>
                    <th>Result</th>
                    <th>Date Executed</th>
                </tr>
                </thead>
                <tbody>
                <g:each var="result" in="${resultModel.recentResults}">
                    <tr>
                        <td><g:link uri="/project/${project.id}/testRun/show/${result.testRun.id}">${result.testRun.name}</g:link></td>
                        <td>
                            <g:if test="${result.result == 'Passed'}">
                                <span class="badge text-bg-success">${result.result}</span>
                            </g:if>
                            <g:elseif test="${result.result == 'Failed'}">
                                <span class="badge text-bg-danger">${result.result}</span>
                            </g:elseif>
                            <g:else>
                                <span class="badge text-bg-secondary">${result.result}</span>
                            </g:else>
                        </td>
                        <td data-name="createdDateValue">${result.dateCreated}</td>
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
