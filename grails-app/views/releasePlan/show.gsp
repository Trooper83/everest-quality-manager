<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'releasePlan.label', default: 'ReleasePlan')}"/>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>

<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate"
                  model="['name':releasePlan.project.name, 'code':releasePlan.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${releasePlan}" var="entity"/>
            <div class="card mt-3">
                <div class="card-header hstack gap-1">
                    <h1 class="me-auto">
                        <g:message code="default.show.label" args="[entityName]"/>
                    </h1>
                    <sec:ifAnyGranted roles="ROLE_BASIC">
                        <g:form resource="${this.releasePlan}" method="DELETE" useToken="true"
                                uri="/project/${this.releasePlan.project.id}/releasePlan/delete/${this.releasePlan.id}">
                            <g:link class="btn btn-primary"
                                    uri="/project/${this.releasePlan.project.id}/releasePlan/edit/${this.releasePlan.id}"
                                    data-test-id="show-edit-link">
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
                                <p id="name-label" class="col-4 fw-bold">Name</p>
                                <p class="col" id="name" aria-labelledby="name-label">${releasePlan.name}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="status-label" class="col-4 fw-bold">Status</p>
                                <p class="col" id="status" aria-labelledby="status-label">${releasePlan.status}</p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="plannedDate-label" class="col-4 fw-bold">Planned Date</p>
                                <p class="col" id="plannedDate">
                                    <g:formatDate format="MMMM d, yyyy" date="${releasePlan.plannedDate}"/>
                                </p>
                            </div>
                        </li>
                        <li class="list-group-item border-bottom">
                            <div class="row">
                                <p id="releaseDate-label" class="col-4 fw-bold">Release Date</p>
                                <p class="col" id="releaseDate">
                                    <g:formatDate format="MMMM d, yyyy" date="${releasePlan.releaseDate}"/>
                                </p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="card mt-3">
                <div class="card-header hstack">
                    <h1 class="me-auto">Test Cycles</h1>
                    <sec:ifAnyGranted roles="ROLE_BASIC">
                        <button id="addTestCycleBtn" type="button" class="btn btn-secondary"
                                data-bs-toggle="modal"
                                data-bs-target="#testCycleModal">Add Test Cycle
                        </button>
                    </sec:ifAnyGranted>
                </div>
                <div class="card-body">
                    <div class="accordion mb-3" id="testCycles">
                        <g:each status="i" var="cycle" in="${releasePlan.testCycles}">
                            <div class="card mb-3">
                                <div class="card-header" id="testCycle-${i}">
                                    <h2 class="mb-0">
                                        <button class="btn btn-link btn-block text-left" type="button"
                                                data-bs-toggle="collapse"
                                                data-bs-target="#collapse-${i}">${cycle.name}
                                        </button>
                                    </h2>
                                </div>
                                <div id="collapse-${i}" class="collapse" data-parent="#testCycles">
                                    <div class="card-body" data-test-id="testCycle-content">
                                        <ul class="list-group list-group-flush">
                                            <li class="list-group-item border-bottom">
                                                <div class="row">
                                                    <p class="col-4 fw-bold">Environment</p>
                                                    <p class="col">${cycle.environ?.name}</p>
                                                </div>
                                            </li>
                                            <li class="list-group-item border-bottom">
                                                <div class="row">
                                                    <p class="col-4 fw-bold">Platform</p>
                                                    <p class="col">${cycle.platform}</p>
                                                </div>
                                            </li>
                                            <li class="list-group-item border-bottom">
                                                <g:link data-test-id="view-test-cycle-link"
                                                        uri="/project/${releasePlan.project.id}/testCycle/show/${cycle.id}">
                                                    view
                                                    test
                                                    cycle
                                                </g:link>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </g:each>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
<div class="modal fade" id="testCycleModal" tabindex="-1" aria-labelledby="testCycleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="testCycleModalLabel">Add Test Cycle</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"
                        data-test-id="modal-close-button">
                </button>
            </div>
            <div class="modal-body">
                <g:form action="addTestCycle" controller="releasePlan" method="POST" useToken="true">
                    <g:hiddenField name="releasePlan.id" value="${releasePlan.id}"/>
                    <div class="row justify-content-center">
                        <div class="col-8 required mb-3">
                            <label class="form-label" for="name">Name</label>
                            <g:textField class="form-control" name="testCycle.name" required="true"
                                         maxLength="100"></g:textField>
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="col-8 mb-3">
                            <label class="form-label" for="environ">Environment</label>
                            <g:select class="form-select" name="testCycle.environ"
                                      from="${releasePlan.project.environments}"
                                      optionKey="id" optionValue="name"
                                      noSelection="${['':'Select an Environment...']}"
                            />
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="col-8 mb-3">
                            <label for="platform">Platform</label>
                            <g:select class="form-select" name="testCycle.platform" from="${['Android', 'iOS', 'Web']}"
                                      noSelection="${['':'Select a Platform...']}"
                            />
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"
                                data-test-id="modal-cancel-button">Cancel
                        </button>
                        <g:submitButton data-test-id="modal-submit-button" class="btn btn-secondary"
                                        name="submit" value="Add Test Cycle"
                        />
                    </div>
                </g:form>
            </div>
        </div>
    </div>
</div>
<asset:javascript src="jquery-3.3.1.min.js"/>
<script>
        $(document).ready(function() {
            $('#testCycleModal').on('hidden.bs.modal', function () {
                $('#testCycleModal form')[0].reset();
            });
        });





</script>
</body>
</html>