<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'bug.label', default: 'Bug')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':bug.project.name, 'code':bug.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${bug}" var="entity"/>
            <g:form resource="${this.bug}" method="PUT" uri="/project/${bug.project.id}/bug/update/${bug.id}" useToken="true">
                <g:hiddenField name="version" value="${this.bug?.version}" />
                <div class="card mt-3">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.edit.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <div class="col-4 mb-3">
                            <label class="form-label" for="status">Status</label>
                            <g:select class="form-select" name="status" from="${['Open', 'Fixed', 'Closed']}" value="${bug.status}"
                            />
                        </div>
                        <div class="col-8 required mb-3">
                            <label class="form-label" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${bug.name}" maxLength="100"></g:textField>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="description">Description</label>
                            <g:textArea class="form-control" name="description" value="${bug.description}" maxLength="500"></g:textArea>
                        </div>
                        <div class="hstack gap-3 mb-3">
                            <div class="col-4">
                                <label class="form-label" for="area">Area</label>
                                <g:select class="form-select" name="area" from="${bug.project.areas}"
                                          optionKey="id" optionValue="name" value="${bug.area?.id}"
                                          noSelection="${['':'']}"
                                />
                            </div>
                            <div class="col-4">
                                <label class="form-label" for="platform">Platform</label>
                                <g:select class="form-select" name="platform" from="${bug.project.platforms}"
                                          noSelection="${[' ':'']}" value="${bug.platform?.id}"
                                          optionKey="id" optionValue="name"
                                />
                            </div>
                        </div>
                        <div class="col-4 mb-3">
                            <label class="form-label" for="environments">Environments</label>
                            <g:select class="form-select" multiple="true" name="environments" from="${bug.project.environments}"
                                      optionKey="id" optionValue="name" value="${bug.environments}"
                                      noSelection="${['':'No Environment...']}"
                                      multiple="true"
                            />
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="notes">Notes</label>
                            <g:textArea class="form-control" name="notes" value="${bug.notes}" maxLength="500"></g:textArea>
                        </div>
                    </div>
                </div>
                <div class="card mt-3 mb-5">
                    <div class="card-header">
                        <h1>Steps To Recreate</h1>
                    </div>
                    <div class="card-body">
                        <g:if test="${bug.steps.size() == 0}">
                            <g:render template="/shared/defaultStepsTemplate"/>
                        </g:if>
                        <g:elseif test="${bug.steps[0].isBuilderStep}">
                            <g:render template="/shared/builderStepsTemplate" bean="${bug}" var="entity"/>
                        </g:elseif>
                        <g:else>
                            <g:render template="/shared/freeFormStepsTemplate" bean="${bug}" var="entity"/>
                        </g:else>
                        <div class="row mb-3 mt-3 me-2 ms-2 border-top">
                            <div class="col-5 mt-3">
                                <label class="form-label" for="expected">Expected</label>
                                <g:textArea class="form-control" name="expected" value="${bug.expected}" maxLength="500"></g:textArea>
                            </div>
                            <div class="col-5 mt-3">
                                <label class="form-label" for="actual">Actual</label>
                                <g:textArea class="form-control" name="actual" value="${bug.actual}" maxLength="500"></g:textArea>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer">
                        <g:submitButton name="update" class="btn btn-primary"
                                        value="${message(code: 'default.button.update.label', default: 'Update')}" />
                    </div>
                </div>
            </g:form>
        </main>
    </div>
</div>
<asset:javascript src="step.js"/>
</body>
</html>
