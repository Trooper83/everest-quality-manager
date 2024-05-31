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
                <h5 class="text-capitalize">${project.name} Home</h5>
                <sec:ifAnyGranted roles="ROLE_BASIC">
                    <g:link role="button" action="show"
                            controller="project" id="${project.id}" class="btn btn-primary ms-auto"
                            elementId="viewButton">View</g:link>
                </sec:ifAnyGranted>
            </div>
            <hr class="mt-3 mb-3"></hr>
            <div class="row">
                <div class="col-md">
                    <div class="card" id="bugCard">
                        <div class="card-body">
                            <h5 class="card-title text-center">Open Bug Count</h5>
                            <h5 class="card-text text-center display-5 text-primary-emphasis">${bugCount}</h5>
                        </div>
                    </div>
                </div>
                <div class="col-md">
                    <div class="card" id="autoTestCard">
                        <div class="card-body">
                            <h5 class="card-title text-center">Automated Test Count</h5>
                            <p class="card-text text-center display-5 text-primary-emphasis">${automatedTestCount}</p>
                        </div>
                    </div>
                </div>
                <div class="col-md">
                    <div class="card" id="testCaseCard">
                        <div class="card-body">
                            <h5 class="card-title text-center">Test Case Count</h5>
                            <p class="card-text text-center display-5 text-primary-emphasis">${testCaseCount}</p>
                        </div>
                    </div>
                </div>
                <div class="col-md">
                    <div class="card" id="scenarioCard">
                        <div class="card-body">
                            <h5 class="card-title text-center">Scenario Count</h5>
                            <p class="card-text text-center display-5 text-primary-emphasis">${scenarioCount}</p>
                        </div>
                    </div>
                </div>
            </div>
            <h5 class="mt-5 mb-1">Release Plans</h5>
            <hr class="mb-3"></hr>
            <div class="row mb-3">
                <div class="col">
                    <div class="card h-100" id="releasedReleasePlanCard">
                        <div class="card-header bg-light">
                            <h5>Released</h5>
                        </div>
                        <div class="card-body">
                            <div class="list-group">
                                <g:if test="${released.size() > 0}">
                                    <g:each var="plan" in="${released}">
                                        <a href="/project/${project.id}/releasePlan/show/${plan.id}"
                                           class="list-group-item list-group-item-action">
                                            <h5 class="mb-2 text-primary">${plan.name}</h5>
                                            <div class="d-flex w-100 justify-content-start">
                                                <small class="me-3 fw-semibold">Released Date:</small>
                                                <small data-name="createdDateValue">${plan.releaseDate}</small>
                                            </div>
                                        </a>
                                    </g:each>
                                </g:if>
                                <g:else>
                                    <p data-test-id="notFoundText">No release plans found</p>
                                </g:else>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col">
                    <div class="card h-100" id="inProgressReleasePlanCard">
                        <div class="card-header bg-light">
                            <h5>In Progress</h5>
                        </div>
                        <div class="card-body">
                            <div class="list-group">
                                <g:if test="${current.size() > 0}">
                                    <g:each var="plan" in="${current}">
                                        <a href="/project/${project.id}/releasePlan/show/${plan.id}"
                                           class="list-group-item list-group-item-action">
                                            <h5 class="mb-2 text-primary">${plan.name}</h5>
                                            <div class="d-flex w-100 justify-content-start">
                                                <small class="me-3 fw-semibold">Planned Date:</small>
                                                <small data-name="createdDateValue">${plan.plannedDate}</small>
                                            </div>
                                        </a>
                                    </g:each>
                                </g:if>
                                <g:else>
                                    <p data-test-id="notFoundText">No release plans found</p>
                                </g:else>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <h5 class="mt-5 mb-1">Bugs</h5>
            <hr class="mb-3"></hr>
            <div class="row mb-3">
                <div class="col">
                    <div class="card" id="recentBugCard">
                        <div class="card-header bg-light">
                            <h5>Recent Bugs</h5>
                        </div>
                        <div class="card-body">
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th scope="col">Name</th>
                                    <th scope="col">Status</th>
                                    <th scope="col">Created</th>
                                    <th scope="col">Created By</th>
                                </tr>
                                </thead>
                                <tbody>
                                <g:each var="bug" in="${recentBugs}">
                                    <tr>
                                        <td><g:link uri="/project/${project.id}/bug/show/${bug.id}">${bug.name}</g:link></td>
                                        <td>${bug.status}</td>
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
        </main>
    </div>
</div>

<asset:javascript src="time.js"/>
</body>
</html>