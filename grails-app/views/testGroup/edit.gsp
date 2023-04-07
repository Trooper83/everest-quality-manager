<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'testGroup.label', default: 'TestGroup')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':testGroup.project.name, 'code':testGroup.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${testGroup}" var="entity"/>
            <g:form resource="${this.testGroup}" method="PUT" useToken="true"
                    uri="/project/${testGroup.project.id}/testGroup/update/${testGroup.id}">
                <g:hiddenField name="version" value="${this.testGroup?.version}" />
                <div class="card mt-3">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.edit.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <div class="col-8 required">
                            <label class="form-label" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${testGroup.name}" maxLength="100"></g:textField>
                        </div>
                    </div>
                    <div class="card-footer">
                        <input class="btn btn-primary" type="submit" data-test-id="edit-update-button"
                               value="${message(code: 'default.button.update.label', default: 'Update')}" />
                    </div>
                </div>
            </g:form>
        </main>
    </div>
</div>
<asset:javascript src="popper.min.js"/>
</body>
</html>
