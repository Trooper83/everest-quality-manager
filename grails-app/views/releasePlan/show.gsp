<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'releasePlan.label', default: 'ReleasePlan')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
    <g:render template="/shared/projectButtonsTemplate"/>
        <a href="#show-releasePlan" class="skip" tabindex="-1">
            <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
        </a>
        <div id="show-releasePlan" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <ol class="property-list releasePlan">
                <li class="fieldcontain">
                    <span id="name-label" class="property-label">Name</span>
                    <div class="property-value" id="name">${releasePlan.name}</div>
                </li>
                <li class="fieldcontain">
                    <span id="project-label" class="property-label">Project</span>
                    <div class="property-value" id="project">${releasePlan.project.name}</div>
                </li>
            </ol>
            <div class="container">
                <h1>Test Cycles</h1>
                <sec:ifAnyGranted roles="ROLE_BASIC">
                    <button id="addTestCycleBtn" type="button" class="btn btn-primary" data-toggle="modal"
                            data-target="#testCycleModal">Add Test Cycle
                    </button>
                </sec:ifAnyGranted>
            <div class="accordion" id="testCycles">
                <g:each status="i" var="cycle" in="${releasePlan.testCycles}">
                <div class="card">
                    <div class="card-header" id="testCycle-${i}">
                        <h2 class="mb-0">
                            <button class="btn btn-link btn-block text-left" type="button" data-toggle="collapse"
                                    data-target="#collapse-${i}">${cycle.name}
                            </button>
                        </h2>
                    </div>
                    <div id="collapse-${i}" class="collapse" data-parent="#testCycles">
                        <div class="card-body" data-test-id="testCycle-content">
                            <div>
                                <label>Environment: </label><span>${cycle.environ?.name}</span>
                            </div>
                            <div>
                                <label>Platform: </label><span>${cycle.platform}</span>
                            </div>
                            <div>
                                <g:link data-test-id="view-test-cycle-link" uri="/project/${releasePlan.project.id}/testCycle/show/${cycle.id}">view test cycle</g:link>
                            </div>
                        </div>
                    </div>
                </div>
                </g:each>
            </div>
            </div>
            <sec:ifAnyGranted roles="ROLE_BASIC">
            <g:form resource="${this.releasePlan}" method="DELETE" params="[projectId: this.releasePlan.project.id]">
                <fieldset class="buttons">
                    <g:link class="edit" uri="/project/${releasePlan.project.id}/releasePlan/edit/${releasePlan.id}" data-test-id="show-edit-link">
                        <g:message code="default.button.edit.label" default="Edit" />
                    </g:link>
                    <input class="delete" type="submit" data-test-id="show-delete-link" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </fieldset>
            </g:form>
            </sec:ifAnyGranted>
        </div>
        <div class="modal fade" id="testCycleModal" tabindex="-1" aria-labelledby="testCycleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="testCycleModalLabel">Add Test Cycle</h5>
                        <button data-test-id="modal-close-button" type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <g:form action="addTestCycle" controller="releasePlan" method="POST">
                            <fieldset class="form">
                                <g:hiddenField name="releasePlan.id" value="${releasePlan.id}" />
                                <div class="fieldcontain required">
                                    <label for="name">Name</label>
                                    <g:field type="text" name="testCycle.name" required="true"/>
                                </div>
                                <div class="fieldcontain">
                                    <label for="environ">Environment</label>
                                    <g:select name="testCycle.environ" from="${releasePlan.project.environments}"
                                              optionKey="id" optionValue="name"
                                              noSelection="${['':'Select an Environment...']}"
                                    />
                                </div>
                                <div class="fieldcontain">
                                    <label for="platform">Platform</label>
                                    <g:select name="testCycle.platform" from="${['Android', 'iOS', 'Web']}"
                                              noSelection="${['':'Select a Platform...']}"
                                    />
                                </div>
                            </fieldset>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal"
                                        data-test-id="modal-cancel-button">Cancel
                                </button>
                                <g:submitButton data-test-id="modal-submit-button" class="btn btn-primary"
                                                name="submit" value="Add Test Cycle"
                                />
                            </div>
                        </g:form>
                    </div>
                </div>
            </div>
        </div>
    <asset:javascript src="popper.min.js"/>
    </body>
</html>