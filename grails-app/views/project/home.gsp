<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
    <title>Project Home</title>
</head>
<body>
<g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
<div id="show-project" class="content scaffold-show col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
    <g:render template="/shared/projectButtonsTemplate"/>
    <div class="row justify-content-end mt-3">
        <sec:ifAnyGranted roles="ROLE_PROJECT_ADMIN">
            <g:link role="button" action="show" controller="project" id="${project.id}" class="btn btn-secondary" elementId="adminButton">Admin</g:link>
        </sec:ifAnyGranted>
    </div>
    <h1>Project Home</h1>
    <g:render template="/shared/messagesTemplate" bean="${project}" var="entity"/>
    <div class="container-fluid mt-5">
        <div class="row row-cols-1">
            <div class="col mb-4">
                <div class="card h-100" id="testCaseCard">
                    <div class="bg-secondary" style="height: 50px; width: 100%;"> </div>
                    <div class="card-body bg-light">
                        <h5 class="card-title">
                            <g:link uri="/project/${project.id}/testCases">Test Cases</g:link>
                        </h5>
                        <p>Count in project: ${testCaseCount}</p>
                    </div>
                </div>
            </div>
            <div class="col mb-4">
                <div class="card h-100" id="scenarioCard">
                    <div class="bg-secondary" style="height: 50px; width: 100%;"> </div>
                    <div class="card-body bg-light">
                        <h5 class="card-title">
                            <g:link uri="/project/${project.id}/scenarios">Scenarios</g:link>
                        </h5>
                        <p>Count in project: ${scenarioCount}</p>
                    </div>
                </div>
            </div>
            <div class="col mb-4">
                <div class="card h-100" id="bugCard">
                    <div class="bg-secondary" style="height: 50px; width: 100%;"> </div>
                    <div class="card-body bg-light">
                        <h5 class="card-title">
                            <g:link uri="/project/${project.id}/bugs">Bugs</g:link>
                        </h5>
                        <p>Count in project: ${bugCount}</p>
                    </div>
                </div>
            </div>
            <div class="col mb-4">
                <div class="card h-100" id="releasePlanCard">
                    <div class="bg-secondary" style="height: 50px; width: 100%;"> </div>
                    <div class="card-body bg-light">
                        <h5 class="card-title">
                            <g:link uri="/project/${project.id}/releasePlans">Release Plans</g:link>
                        </h5>
                        <p>Previous Release:
                            <g:link elementId="previousLink" uri="/project/${project.id}/releasePlan/show/${previousRelease?.id}">${previousRelease?.name}</g:link>
                        </p>
                        <p>Next Release:
                            <g:link elementId="nextLink" uri="/project/${project.id}/releasePlan/show/${nextRelease?.id}">${nextRelease?.name}</g:link>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<asset:javascript src="popper.min.js"/>
</body>
</html>