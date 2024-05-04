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
            <div class="card mt-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h3 class="card-title">Project Home</h3>
                    <sec:ifAnyGranted roles="ROLE_BASIC">
                        <g:link role="button" action="show"
                                controller="project" id="${project.id}" class="btn btn-primary ml-3"
                                elementId="viewButton">View</g:link>
                    </sec:ifAnyGranted>
                </div>
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 m-3">
                    <div class="col">
                        <div class="card bg-light h-100" style="max-width: 18rem;" id="testCaseCard">
                            <div class="card-header bg-secondary"></div>
                            <div class="card-body">
                                <h5 class="card-title">
                                    <g:link uri="/project/${project.id}/testCases">Test Cases</g:link>
                                </h5>
                                <p class="card-text">Count in project: ${testCaseCount}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card bg-light h-100" style="max-width: 18rem;" id="autoTestCard">
                            <div class="card-header bg-secondary"></div>
                            <div class="card-body">
                                <h5 class="card-title">
                                    <g:link uri="/project/${project.id}/automatedTests">Automated Tests</g:link>
                                </h5>
                                <p class="card-text">Count in project: ${automatedTestCount}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card bg-light h-100" style="max-width: 18rem;" id="bugCard">
                            <div class="card-header bg-secondary"></div>
                            <div class="card-body">
                                <h5 class="card-title">
                                    <g:link uri="/project/${project.id}/bugs">Bugs</g:link>
                                </h5>
                                <p class="card-text">Count in project: ${bugCount}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card bg-light h-100" style="max-width: 18rem;" id="releasePlanCard">
                            <div class="card-header bg-secondary"></div>
                            <div class="card-body">
                                <h5 class="card-title">
                                    <g:link uri="/project/${project.id}/releasePlans">Release Plans</g:link>
                                </h5>
                                <p class="card-text">Previous Release:
                                    <g:link elementId="previousLink" uri="/project/${project.id}/releasePlan/show/${previousRelease?.id}">${previousRelease?.name}</g:link>
                                </p>
                                <p class="card-text">Next Release:
                                    <g:link elementId="nextLink" uri="/project/${project.id}/releasePlan/show/${nextRelease?.id}">${nextRelease?.name}</g:link>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>