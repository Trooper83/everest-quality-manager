<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'step.label', default: 'Step')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':step.project.name, 'code':step.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${step}" var="entity"/>
            <g:form resource="${this.step}" method="PUT"
                    uri="/project/${step.project.id}/step/update/${step.id}" useToken="true">
                <g:hiddenField name="version" value="${this.step?.version}" />
                <div class="card mt-3 mb-5">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.edit.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <g:hiddenField name="project" value="${step.project.id}"/>
                        <div class="col-8 required mb-3">
                            <label class="form-label" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${step.name}" maxLength="100"></g:textField>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="act">Action</label>
                            <g:textArea class="form-control" name="act" value="${step.act}" maxLength="500"></g:textArea>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="result">Result</label>
                            <g:textArea class="form-control" name="result" value="${step.result}" maxLength="500"></g:textArea>
                        </div>
                    </div>
                    <div class="card-footer">
                        <g:submitButton data-test-id="edit-update-button" name="update" class="btn btn-primary" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                    </div>
                </div>
            </g:form>
        </main>
    </div>
</div>
</body>
</html>
