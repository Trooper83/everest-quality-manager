<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'testGroup.label', default: 'TestGroup')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${testGroup}" var="entity"/>
            <g:form resource="${this.testGroup}" method="POST" uri="/project/${project.id}/testGroup/save" useToken="true">
                <div class="card mt-3">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.create.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <g:hiddenField name="project" value="${project.id}"/>
                        <div class="col-8 required">
                            <label class="form-label" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${testGroup.name}" maxLength="100"></g:textField>
                        </div>
                    </div>
                    <div class="card-footer">
                        <fieldset class="buttons">
                            <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                        </fieldset>
                    </div>
                </div>
            </g:form>
        </main>
    </div>
</div>
</body>
</html>
