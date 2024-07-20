<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'bug.label', default: 'Bug')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${bug}" var="entity"/>
            <g:form resource="${this.bug}" method="POST" uri="/project/${project.id}/bug/save" useToken="true">
                <div class="card mt-3">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.create.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <g:hiddenField name="project" value="${project.id}"/>
                        <g:hiddenField name="status" value="Open"/>
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
                                <g:select class="form-select" name="area" from="${project.areas}"
                                          optionKey="id" optionValue="name"
                                          noSelection="${['':'']}"
                                />
                            </div>
                            <div class="col-4">
                                <label class="form-label" for="platform">Platform</label>
                                <g:select class="form-select" name="platform" from="${project.platforms}"
                                          optionKey="id" optionValue="name"
                                          noSelection="${['':'']}"
                                />
                            </div>
                        </div>
                        <div class="col-4 mb-3">
                            <label class="form-label" for="environments">Environments</label>
                            <g:select class="form-select" multiple="true" name="environments" from="${project.environments}"
                                      optionKey="id" optionValue="name"
                                      noSelection="${['':'Select Environments...']}"
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
                        <g:render template="/shared/createStepsTemplate"/>
                        <div class="row mb-3 mt-3 ms-2 me-2 border-top">
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
                        <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                    </div>
                </div>
            </g:form>
        </main>
        <g:render template="/shared/toastTemplate"/>
    </div>
</div>
<g:render template="/shared/stepsModal"/>
<asset:javascript src="step.js"/>
<script>
    $(document).ready(function() {
        $('#stepsModal').on('hidden.bs.modal', function () {
            resetForm();
        });
    });
</script>
</body>
</html>
