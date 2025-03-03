<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testCase.label', default: 'TestCase')}"/>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':testCase.project.name, 'code':testCase.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${testCase}" var="entity"/>
            <div class="card mt-3">
                <div class="card-header hstack gap-1">
                    <h1 class="me-auto">
                        <g:message code="default.show.label" args="[entityName]"/>
                    </h1>
                    <sec:ifAnyGranted roles="ROLE_BASIC">
                        <g:form resource="${this.testCase}" method="DELETE" useToken="true"
                                uri="/project/${this.testCase.project.id}/testCase/delete/${this.testCase.id}">
                            <g:link class="btn btn-primary" uri="/project/${this.testCase.project.id}/testCase/edit/${this.testCase.id}" data-test-id="show-edit-link">
                                <g:message code="default.button.edit.label" default="Edit"/>
                            </g:link>
                            <input class="btn btn-secondary" type="submit" data-test-id="show-delete-link"
                                   value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                   onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
                        </g:form>
                    </sec:ifAnyGranted>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="person-label" class="col-4 fw-bold">Created By</p>
                                <p class="col" id="person" aria-labelledby="person-label">${testCase.person.email}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="name-label" class="col-4 fw-bold">Name</p>
                                <p class="col" id="name" aria-labelledby="name-label">${testCase.name}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="description-label" class="col-4 fw-bold">Description</p>
                                <p class="col" id="description" aria-labelledby="description-label">${testCase.description}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="area-label" class="col-4 fw-bold">Area</p>
                                <p class="col" id="area" aria-labelledby="area-label">${testCase.area?.name}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="platform-label" class="col-4 fw-bold">Platform</p>
                                <p class="col" id="platform" aria-labelledby="platform-label">${testCase.platform?.name}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="type-label" class="col-4 fw-bold">Type</p>
                                <p class="col" id="type" aria-labelledby="type-label">${testCase.type}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="executionMethod-label" class="col-4 fw-bold">Execution Method</p>
                                <p class="col" id="executionMethod" aria-labelledby="executionMethod-label">${testCase.executionMethod}</p>
                            </div>
                        </li>
                        <li class="list-group-item">
                            <div class="row align-items-center">
                                <p id="environments-label" class="col-4 fw-bold">Environments</p>
                                <div class="col form-row" id="environments">
                                    <g:each in="${testCase.environments}">
                                        <p>${it.name}</p>
                                    </g:each>
                                </div>
                            </div>
                        </li>
                        <li class="list-group-item">
                            <div class="row align-items-center" id="testGroups">
                                <p id="testGroups-label" class="col-4 fw-bold">Test Groups</p>
                                <div class="col form-row">
                                    <g:each in="${testCase.testGroups}">
                                        <p>${it.name}</p>
                                    </g:each>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="card mt-3 mb-5">
                <div class="card-header">
                    <h1 class="me-auto">Steps</h1>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col-4 fw-bold">Action</p>
                                <p class="col-4 fw-bold">Data</p>
                                <p class="col-4 fw-bold">Result</p>
                            </div>
                        </li>
                    </ul>
                    <g:render template="/shared/showStepsTableTemplate" bean="${testCase}" var="entity"/>
                    <ul class="list-group list-group-flush mt-3">
                        <li class="list-group-item border-bottom mt-4">
                            <div class="row">
                                <p class="col-4 fw-bold">Verify</p>
                            </div>
                        </li>
                    </ul>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item">
                            <div class="row">
                                <p id="verify" class="col-4">${testCase.verify}</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>
