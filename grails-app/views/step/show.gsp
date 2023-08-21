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
                    </ul>
                    <ul class="list-group list-group-flush mt-3">
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p class="col-6 fw-bold">Action</p>
                                <p class="col-6 fw-bold">Result</p>
                            </div>
                        </li>
                    </ul>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item">
                            <div class="row">
                                <p id="action" class="col-6">${step.action}</p>
                                <p id="result" class="col-6">${step.result}</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="card mt-3">
                <div class="card-header">
                    <h1>Step Path</h1>
                </div>
                <div class="card-body">
                    <g:each in="${step.linkedSteps}">
                        <p>${it.name}</p>
                    </g:each>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>
