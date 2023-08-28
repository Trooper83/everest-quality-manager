<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'step.label', default: 'Step')}"/>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':step.project.name, 'code':step.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${step}" var="entity"/>
            <div class="card mt-3">
                <div class="card-header hstack gap-1">
                    <h1 class="me-auto">
                        <g:message code="default.show.label" args="[entityName]"/>
                    </h1>
                    <sec:ifAnyGranted roles="ROLE_BASIC">
                        <g:form resource="${this.step}" method="DELETE" useToken="true"
                                uri="/project/${this.step.project.id}/step/delete/${this.step.id}">
                            <g:link class="btn btn-primary" uri="/project/${this.step.project.id}/step/edit/${this.step.id}" data-test-id="show-edit-link">
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
                                <p class="col" id="person" aria-labelledby="person-label">${step.person.email}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="name-label" class="col-4 fw-bold">Name</p>
                                <p class="col" id="name" aria-labelledby="name-label">${step.name}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="action-label" class="col-4 fw-bold">Action</p>
                                <p id="act" class="col" aria-labelledby="action-label">${step.act}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="result-label" class="col-4 fw-bold">Result</p>
                                <p id="result" class="col" aria-labelledby="result-label">${step.result}</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="card mt-3">
                <div class="card-header">
                    <h1>Related Steps</h1>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item border-bottom-0">
                            <div class="row" id="parents">
                                <p class="fw-bold border-bottom">Parents</p>
                                <g:if test="${relations.parents.size() == 0}">
                                    <p>No parent steps found</p>
                                </g:if>
                                <g:each in="${relations.parents}">
                                    <div class="row mt-1">
                                        <p class="col-6">${it.linkedStep.name}</p>
                                    </div>
                                </g:each>
                            </div>
                        </li>
                        <li class="list-group-item mt-3">
                            <div class="row" id="children">
                                <p class="fw-bold border-bottom">Children</p>
                                <g:if test="${relations.children.size() == 0}">
                                    <p>No child steps found</p>
                                </g:if>
                                <g:each in="${relations.children}">
                                    <div class="row">
                                        <p class="col-6">${it.linkedStep.name}</p>
                                    </div>
                                </g:each>
                            </div>
                        </li>
                        <li class="list-group-item mt-3">
                            <div class="row" id="siblings">
                                <p class="fw-bold border-bottom">Siblings</p>
                                <g:if test="${relations.siblings.size() == 0}">
                                    <p>No sibling steps found</p>
                                </g:if>
                                <g:each in="${relations.siblings}">
                                    <div class="row">
                                        <p class="col-6">${it.linkedStep.name}</p>
                                    </div>
                                </g:each>
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
