<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
    <title>Project Details</title>
</head>
<body>
<div class="container-fluid">
    <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
    <div role="main" class="container">
        <div class="row justify-content-center mt-3">
            <div class="col-10">
                <g:render template="/shared/messagesTemplate" bean="${project}" var="entity"/>
            </div>
        </div>
            <div class="row justify-content-center">
            <div class="col-8">
                <div class="card mt-5">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.show.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <ul class="list-group list-group-flush">
                            <li class="list-group-item">
                                <div class="row">
                                    <p id=code-label class="col-4 font-weight-bold">Code</p>
                                    <p class="col" id="code" aria-labelledby="code-label">${project.code}</p>
                                </div>
                            </li>
                        </ul>
                        <ul class="list-group list-group-flush">
                            <li class="list-group-item">
                                <div class="row">
                                    <p id=name-label class="col-4 font-weight-bold">Name</p>
                                    <p class="col" id="name" aria-labelledby="name-label">${project.name}</p>
                                </div>
                            </li>
                        </ul>
                        <ul class="list-group list-group-flush">
                            <li class="list-group-item">
                                <div class="row align-items-center" id="areas">
                                    <p id=areas-label class="col-4 font-weight-bold">Areas</p>
                                    <div class="col form-row mb-3">
                                        <g:each in="${project.areas}">
                                            <h3>
                                                <p class="ml-1 font-weight-normal badge badge-light border border-secondary border-rounded">${it.name}</p>
                                            </h3>
                                        </g:each>
                                    </div>
                                </div>
                            </li>
                        </ul>
                        <ul class="list-group list-group-flush">
                            <li class="list-group-item">
                                <div class="row align-items-center" id="environments">
                                    <p id=environments-label class="col-4 font-weight-bold">Environments</p>
                                    <div class="col form-row">
                                        <g:each in="${project.environments}">
                                            <h3>
                                                <p class="ml-1 font-weight-normal badge badge-light border border-secondary border-rounded">${it.name}</p>
                                            </h3>
                                        </g:each>
                                    </div>
                                </div>
                            </li>
                        </ul>
                        <div class="row mt-3">
                            <sec:ifAnyGranted roles="ROLE_PROJECT_ADMIN">
                                <g:form resource="${this.project}" method="DELETE" useToken="true">
                                    <div class="col">
                                        <g:link class="btn btn-primary" action="edit" resource="${this.project}" data-test-id="show-edit-link">
                                            <g:message code="default.button.edit.label" default="Edit" />
                                        </g:link>
                                        <input class="btn" type="submit" data-test-id="show-delete-link" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                                    </div>
                                </g:form>
                            </sec:ifAnyGranted>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<asset:javascript src="popper.min.js"/>
</body>
</html>