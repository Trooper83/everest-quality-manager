<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'scenario.label', default: 'Scenario')}"/>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':scenario.project.name, 'code':scenario.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${scenario}" var="entity"/>
            <div class="card mt-3">
                <div class="card-header hstack gap-1">
                    <h1 class="me-auto">
                        <g:message code="default.show.label" args="[entityName]"/>
                    </h1>
                    <sec:ifAnyGranted roles="ROLE_BASIC">
                        <g:form resource="${this.scenario}" method="DELETE" useToken="true"
                                uri="/project/${this.scenario.project.id}/scenario/delete/${this.scenario.id}">
                            <g:link class="btn btn-primary" uri="/project/${this.scenario.project.id}/scenario/edit/${this.scenario.id}" data-test-id="show-edit-link">
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
                                <p class="col" id="person" aria-labelledby="person-label">${scenario.person.email}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="name-label" class="col-4 fw-bold">Name</p>
                                <p class="col" id="name" aria-labelledby="name-label">${scenario.name}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="description-label" class="col-4 fw-bold">Description</p>
                                <p class="col" id="description" aria-labelledby="description-label">${scenario.description}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="area-label" class="col-4 fw-bold">Area</p>
                                <p class="col" id="area" aria-labelledby="area-label">${scenario.area?.name}</p>
                            </div>
                        </li>
                        <li class="list-group-item">
                            <div class="row align-items-center" id="environments">
                                <p id="environments-label" class="col-4 fw-bold">Environments</p>
                                <div class="col form-row">
                                    <g:each in="${scenario.environments}">
                                        <p>${it.name}</p>
                                    </g:each>
                                </div>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="executionMethod-label" class="col-4 fw-bold">Execution Method</p>
                                <p class="col" id="executionMethod" aria-labelledby="executionMethod-label">${scenario.executionMethod}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="type-label" class="col-4 fw-bold">Type</p>
                                <p class="col" id="type" aria-labelledby="type-label">${scenario.type}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="platform-label" class="col-4 fw-bold">Platform</p>
                                <p class="col" id="platform" aria-labelledby="platform-label">${scenario.platform}</p>
                            </div>
                        </li>
                        <li class="list-group-item">
                            <div class="row">
                                <p id="gherkin-label" class="col-4 fw-bold">Gherkin</p>
                                <p class="col" id="gherkin" aria-labelledby="gherkin-label">${scenario.gherkin}</p>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="card-footer">
                </div>
            </div>
        </main>
    </div>
</div>
</div>
</body>
</html>
