<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
    <title>Project Home</title>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main id="show-project" class="content scaffold-show col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
            <g:render template="/shared/messagesTemplate" bean="${project}" var="entity"/>
            <div class="container-fluid mt-5">
                <div class="card">
                    <div class="card-header mb-4 d-flex justify-content-between align-items-center">
                        <h3 class="card-title">Project Home</h3>
                        <sec:ifAnyGranted roles="ROLE_PROJECT_ADMIN">
                            <g:link role="button" action="show"
                                    controller="project" id="${project.id}" class="btn btn-secondary ml-3"
                                    elementId="adminButton">View</g:link>
                        </sec:ifAnyGranted>
                    </div>
                    <div class="card-deck">
                        <div class="col mb-4">
                            <div class="card bg-light mb-3 h-100" style="max-width: 18rem;" id="testCaseCard">
                                <div class="card-header bg-secondary"></div>
                                <div class="card-body">
                                    <h5 class="card-title">
                                        <g:link uri="/project/${project.id}/testCases">Test Cases</g:link>
                                    </h5>
                                    <p class="card-text">Count in project: ${testCaseCount}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col mb-4">
                            <div class="card bg-light mb-3 h-100" style="max-width: 18rem;" id="scenarioCard">
                                <div class="card-header bg-secondary"></div>
                                <div class="card-body">
                                    <h5 class="card-title">
                                        <g:link uri="/project/${project.id}/scenarios">Scenarios</g:link>
                                    </h5>
                                    <p class="card-text">Count in project: ${scenarioCount}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col mb-4">
                            <div class="card bg-light mb-3 h-100" style="max-width: 18rem;" id="bugCard">
                                <div class="card-header bg-secondary"></div>
                                <div class="card-body">
                                    <h5 class="card-title">
                                        <g:link uri="/project/${project.id}/bugs">Bugs</g:link>
                                    </h5>
                                    <p class="card-text">Count in project: ${bugCount}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col mb-4">
                            <div class="card bg-light mb-3 h-100" style="max-width: 18rem;" id="releasePlanCard">
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
            </div>
        </main>
    </div>
</div>
<asset:javascript src="popper.min.js"/>
</body>
</html>