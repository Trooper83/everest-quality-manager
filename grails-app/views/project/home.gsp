<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
    <title>Project Home</title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto">
            <div class="mt-3 hstack">
                <h1 class="text-capitalize">${project.name} Home</h1>
                <sec:ifAnyGranted roles="ROLE_BASIC">
                    <g:link role="button" action="show"
                            controller="project" id="${project.id}" class="btn btn-primary ms-auto"
                            elementId="viewButton">View</g:link>
                </sec:ifAnyGranted>
            </div>
            <hr class="mt-2 mb-2"></hr>
            <div class="row">
                <div class="col-md">
                    <div class="card bg-light" id="autoTestCard">
                        <div class="card-body">
                            <h5 class="card-title">Automated Tests</h5>
                            <p class="card-text">Count in project: ${automatedTestCount}</p>
                        </div>
                    </div>
                </div>
                <div class="col-md">
                    <div class="card bg-light" id="bugCard">
                        <div class="card-body">
                            <h5 class="card-title">Bugs</h5>
                            <p class="card-text">Count in project: ${bugCount}</p>
                        </div>
                    </div>
                </div>
                <div class="col-md">
                    <div class="card bg-light" id="testCaseCard">
                        <div class="card-body">
                            <h5 class="card-title">Test Cases</h5>
                            <p class="card-text">Count in project: ${testCaseCount}</p>
                        </div>
                    </div>
                </div>
                <div class="col-md">
                    <div class="card bg-light" id="scenarioCard">
                        <div class="card-body">
                            <h5 class="card-title">Scenarios</h5>
                            <p class="card-text">Count in project: ${scenarioCount}</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row mt-3 mb-3">
                <div class="col">
                    <div class="card bg-light" id="releasePlanCard">
                        <div class="card-body">
                            <h5 class="card-title">Release Plans</h5>
                            <div class="row">
                                <div class="col">
                                    <ul class="list-group">
                                        <g:each var="plan" in="${released}">
                                            <li class="list-group-item">
                                                <g:link uri="/project/${project.id}/releasePlan/show/${plan.id}">${plan.name}</g:link>
                                            </li>
                                        </g:each>
                                    </ul>
                                </div>
                                <div class="col">
                                    <ul class="list-group">
                                    <g:each var="plan" in="${current}">
                                        <li class="list-group-item">
                                            <g:link uri="/project/${project.id}/releasePlan/show/${plan.id}">${plan.name}</g:link>
                                        </li>
                                    </g:each>
                                    </ul>
                                </div>
                                <div class="col">
                                    <ul class="list-group">
                                    <g:each var="plan" in="${next}">
                                        <li class="list-group-item">
                                            <g:link uri="/project/${project.id}/releasePlan/show/${plan.id}">${plan.name}</g:link>
                                        </li>
                                    </g:each>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row mb-3">
                <div class="col">
                    <div class="card bg-light" id="recentBugCard">
                        <div class="card-body">
                            <h5 class="card-title">Recent Bugs</h5>
                            <table class="table">
                                <thead>
                                <tr>
                                    <th scope="col">Name</th>
                                    <th scope="col">Created</th>
                                    <th scope="col">Created By</th>
                                </tr>
                                </thead>
                                <tbody>
                                <g:each var="bug" in="${recentBugs}">
                                    <tr>
                                        <td><g:link uri="/project/${project.id}/bug/show/${bug.id}">${bug.name}</g:link></td>
                                        <td data-name="createdDateValue">${bug.dateCreated}</td>
                                        <td>${bug.person.email}</td>
                                    </tr>
                                </g:each>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row mb-3">
                <div class="col">
                    <div class="card bg-light me-3 h-100" id="testRunCard">
                        <div class="card-body">
                            <h5 class="card-title">Recent Test Runs</h5>
                            <table class="table">
                                <thead>
                                <tr>
                                    <th scope="col">Name</th>
                                    <th scope="col">Date Executed</th>
                                </tr>
                                </thead>
                                <tbody>
                                <g:each var="run" in="${testRuns}">
                                    <tr>
                                        <td><g:link uri="/project/${project.id}/testRun/show/${run.id}">${run.name}</g:link></td>
                                        <td data-name="createdDateValue">${run.dateCreated}</td>
                                    </tr>
                                </g:each>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="col">
                    <div class="card bg-light h-100" id="mostFailedAutoTestCard">
                        <div class="card-body">
                            <h5 class="card-title">Most Failed Automated Tests</h5>
                            <table class="table">
                                <thead>
                                <tr>
                                    <th scope="col">Name</th>
                                    <th scope="col">Count</th>
                                </tr>
                                </thead>
                                <tbody>
                                <g:each var="test" in="${mostFailedTests}">
                                    <tr>
                                        <td><g:link uri="/project/${project.id}/automatedTest/show/${test[0]}">${test[1]}</g:link></td>
                                        <td>${test[2]}</td>
                                    </tr>
                                </g:each>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>

<asset:javascript src="time.js"/>
</body>
</html>