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
                                          noSelection="${['':'No Environments...']}"
                                          multiple="true"
                                />
                            </div>
                            <div class="col-4">
                                <label class="form-label" for="testGroups">Test Groups</label>
                                <g:select name="testGroups" from="${testCase.project.testGroups}"
                                          optionKey="id" optionValue="name"
                                          noSelection="${['':'No Test Groups...']}"
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
                        <div class="row border-bottom">
                            <p class="col-5">Action</p>
                            <p class="col-5">Result</p>
                        </div>
                        <div id="stepsTableContent">
                            <g:render template="/shared/editStepsTableTemplate" bean="${testCase}" var="entity"/>
                        </div>
                        <input class="btn btn-secondary btn-sm mt-3" id="btnAddRow" type="button" value="Add Step"
                               onclick="addEntryRow()" accesskey="n"/>
                        <asset:image src="icons/info.svg" alt="info" width="15" height="15"
                                     data-toggle="tooltip" data-placement="top" title="ALT+n to add a new row"/>
                    </div>
                    <div class="card-footer">
                        <g:submitButton name="Update" data-test-id="edit-update-button" class="btn btn-primary"
                                        value="${message(code: 'default.button.update.label', default: 'Update')}"/>
                    </div>
                </div>
            </g:form>
        </main>
    </div>
</div>
<asset:javascript src="step.js"/>
</body>
</html>
