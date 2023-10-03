<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value='${message(code: "testCase.label", default: "TestCase")}' />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${testCase}" var="entity"/>
            <g:form resource="${this.testCase}" method="POST" uri="/project/${project.id}/testCase/save" useToken="true">
                <div class="card mt-3">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.create.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <g:hiddenField name="project" value="${project.id}"/>
                        <div class="col-8 required mb-3">
                            <label class="form-label" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${testCase.name}" maxLength="100"></g:textField>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="description">Description</label>
                            <g:textArea class="form-control" name="description" value="${testCase.description}" maxLength="500"></g:textArea>
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
                                <g:select class="form-select" name="platform" from="${['Android', 'iOS', 'Web']}"
                                          noSelection="${['':'']}"
                                />
                            </div>
                        </div>
                        <div class="hstack gap-3 mb-3">
                            <div class="col-4">
                                <label class="form-label" for="type">Type</label>
                                <g:select class="form-select" name="type" from="${['API', 'UI']}"
                                          noSelection="${['':'']}"
                                />
                            </div>
                            <div class="col-4">
                                <label class="form-label" for="executionMethod">Execution Method</label>
                                <g:select class="form-select" name="executionMethod" from="${['Automated', 'Manual']}"
                                          noSelection="${['':'']}"
                                />
                            </div>
                        </div>
                        <div class="hstack gap-3 mb-3">
                            <div class="col-4">
                                <label class="form-label" for="environments">Environments</label>
                                <g:select class="form-select" multiple="true" name="environments" from="${project.environments}"
                                          optionKey="id" optionValue="name"
                                          noSelection="${['':'Select Environments...']}"
                                          multiple="true"
                                />
                            </div>
                            <div class="col-4">
                                <label class="form-label" for="testGroups">Test Groups</label>
                                <g:select name="testGroups" from="${project.testGroups}"
                                          optionKey="id" optionValue="name"
                                          noSelection="${['':'Select Test Groups...']}"
                                          multiple="true" class="form-select"
                                />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card mt-3 mb-5">
                    <div class="card-header">
                        <h1>Steps</h1>
                    </div>
                    <div class="card-body">
                        <g:render template="/shared/defaultStepsTemplate"/>
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
<asset:javascript src="step.js"/>
</body>
</html>
