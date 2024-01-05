<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testCase.label', default: 'TestCase')}"/>
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate"
                  model="['name':testCase.project.name, 'code':testCase.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${testCase}" var="entity"/>
            <g:form resource="${this.testCase}" method="PUT"
                    uri="/project/${testCase.project.id}/testCase/update/${testCase.id}" useToken="true">
                <g:hiddenField name="version" value="${this.testCase?.version}"/>
                <div class="card mt-3">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.edit.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <div class="col-8 required mb-3">
                            <label class="form-label" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${testCase.name}"
                                         maxLength="100"></g:textField>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="description">Description</label>
                            <g:textArea class="form-control" name="description" value="${testCase.description}"
                                        maxLength="500"></g:textArea>
                        </div>
                        <div class="hstack gap-3 mb-3">
                            <div class="col-4">
                                <label class="form-label" for="area">Area</label>
                                <g:select class="form-select" name="area" from="${testCase.project.areas}"
                                          optionKey="id" optionValue="name" value="${testCase.area?.id}"
                                          noSelection="${['':'']}"
                                />
                            </div>
                            <div class="col-4">
                                <label class="form-label" for="platform">Platform</label>
                                <g:select class="form-select" name="platform" from="${['Android', 'iOS', 'Web']}"
                                          noSelection="${['':'']}" value="${testCase.platform}"
                                />
                            </div>
                        </div>
                        <div class="hstack gap-3 mb-3">
                            <div class="col-4">
                                <label class="form-label" for="type">Type</label>
                                <g:select class="form-select" name="type" from="${['API', 'UI']}"
                                          noSelection="${['':'']}" value="${testCase.type}"
                                />
                            </div>
                            <div class="col-4">
                                <label class="form-label" for="executionMethod">Execution Method</label>
                                <g:select class="form-select" name="executionMethod" from="${['Automated', 'Manual']}"
                                          noSelection="${['':'']}" value="${testCase.executionMethod}"
                                />
                            </div>
                        </div>
                        <div class="hstack gap-3 mb-3">
                            <div class="col-4">
                                <label class="form-label" for="environments">Environments</label>
                                <g:select class="form-select" multiple="true" name="environments"
                                          from="${testCase.project.environments}"
                                          optionKey="id" optionValue="name"
                                          noSelection="${['':'No Environment...']}"
                                          multiple="true" value="${testCase.environments}"
                                />
                            </div>
                            <div class="col-4">
                                <label class="form-label" for="testGroups">Test Groups</label>
                                <g:select name="testGroups" from="${testCase.project.testGroups}"
                                          optionKey="id" optionValue="name" value="${testCase.testGroups}"
                                          noSelection="${['':'No Test Group...']}"
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
                        <g:if test="${testCase.steps.size() == 0}">
                            <g:render template="/shared/defaultStepsTemplate"/>
                        </g:if>
                        <g:elseif test="${testCase.steps[0].isBuilderStep}">
                            <g:render template="/shared/builderStepsTemplate" bean="${testCase}" var="entity"/>
                        </g:elseif>
                        <g:else>
                            <g:render template="/shared/freeFormStepsTemplate" bean="${testCase}" var="entity"/>
                        </g:else>
                        <div class="row mb-3 mt-3 me-2 ms-2 border-top">
                            <div class="col-8 mt-3">
                                <label class="form-label" for="verify">Verify</label>
                                <g:textArea class="form-control" name="verify" value="${testCase.verify}" maxLength="500"></g:textArea>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer">
                        <g:submitButton name="Update" data-test-id="edit-update-button" class="btn btn-primary"
                                        value="${message(code: 'default.button.update.label', default: 'Update')}"/>
                    </div>
                </div>
            </g:form>
        </main>
    </div>
    <g:render template="/shared/toastTemplate"/>
</div>
<asset:javascript src="step.js"/>
</body>
</html>
