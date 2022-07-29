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
    <h1>Project Home</h1>
    <g:render template="/shared/messagesTemplate" bean="${project}" var="entity"/>
    <div class="container-fluid mt-5">
        <div class="row row-cols-1">
            <div class="col mb-4">
                <div class="card h-100">
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
                <div class="card h-100">
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
                <div class="card h-100">
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
                <div class="card h-100">
                    <div class="bg-secondary" style="height: 50px; width: 100%;"> </div>
                    <div class="card-body bg-light">
                        <h5 class="card-title">
                            <g:link uri="/project/${project.id}/releasePlans">Release Plans</g:link>
                        </h5>
                        <p>Last Release</p>
                        <p>Next Release</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <sec:ifAnyGranted roles="ROLE_PROJECT_ADMIN">
        <g:form resource="${this.project}" method="DELETE">
            <fieldset class="buttons">
                <g:link class="edit" action="edit" resource="${this.project}" data-test-id="home-edit-link">
                    <g:message code="default.button.edit.label" default="Edit" />
                </g:link>
                <input class="delete" type="submit" data-test-id="home-delete-link" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
            </fieldset>
        </g:form>
    </sec:ifAnyGranted>
</div>
<asset:javascript src="popper.min.js"/>
</body>
</html>