<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
    <title>Project Details</title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${project}" var="entity"/>
            <div class="card mt-3">
                <div class="card-header hstack">
                    <h1>
                        <g:message code="default.show.label" args="[entityName]"/>
                    </h1>
                    <sec:ifAnyGranted roles="ROLE_PROJECT_ADMIN">
                        <g:form class="ms-auto" resource="${this.project}" method="DELETE" useToken="true">
                            <div class="col">
                                <g:link class="btn btn-primary" action="edit" resource="${this.project}" data-test-id="show-edit-link">
                                    <g:message code="default.button.edit.label" default="Edit" />
                                </g:link>
                                <input class="btn btn-secondary" type="submit" data-test-id="show-delete-link" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                            </div>
                        </g:form>
                    </sec:ifAnyGranted>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush border-bottom">
                        <li class="list-group-item">
                            <div class="row">
                                <p id="code-label" class="col-4 fw-bold">Code</p>
                                <p class="col" id="code" aria-labelledby="code-label">${project.code}</p>
                            </div>
                        </li>
                    </ul>
                    <ul class="list-group list-group-flush border-bottom">
                        <li class="list-group-item">
                            <div class="row">
                                <p id="name-label" class="col-4 fw-bold">Name</p>
                                <p class="col" id="name" aria-labelledby="name-label">${project.name}</p>
                            </div>
                        </li>
                    </ul>
                    <ul class="list-group list-group-flush border-bottom">
                        <li class="list-group-item">
                            <div class="row align-items-center" id="areas">
                                <p id="areas-label" class="col-4 fw-bold">Areas</p>
                                <div class="col form-row">
                                    <g:each in="${project.areas}">
                                        <p class="ml-1 badge text-bg-light border">${it.name}</p>
                                    </g:each>
                                </div>
                            </div>
                        </li>
                    </ul>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item">
                            <div class="row align-items-center" id="environments">
                                <p id="environments-label" class="col-4 fw-bold">Environments</p>
                                <div class="col form-row">
                                    <g:each in="${project.environments}">
                                        <p class="ml-1 badge text-bg-light border">${it.name}</p>
                                    </g:each>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </main>
    </div>
</div>
</div>
</div>
</body>
</html>