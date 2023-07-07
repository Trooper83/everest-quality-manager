<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'scenario.label', default: 'Scenario')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':scenario.project.name, 'code':scenario.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${scenario}" var="entity"/>
            <g:form resource="${this.scenario}" method="PUT"
                    uri="/project/${scenario.project.id}/scenario/update/${scenario.id}" useToken="true">
                <g:hiddenField name="version" value="${this.scenario?.version}" />
                <div class="card mt-3 mb-5">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.edit.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <g:hiddenField name="project" value="${scenario.project.id}"/>
                        <div class="col-8 required mb-3">
                            <label class="form-label" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${scenario.name}" maxLength="100"></g:textField>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="description">Description</label>
                            <g:textArea class="form-control" name="description" value="${scenario.description}" maxLength="500"></g:textArea>
                        </div>
                        <div class="hstack gap-3 mb-3">
                            <div class="col-4">
                                <label class="form-label" for="area">Area</label>
                                <g:select class="form-select" name="area" id="area" from="${scenario.project.areas}"
                                          optionKey="id" optionValue="name" value="${scenario.area?.id}"
                                          noSelection="${['':'']}"
                                />
                            </div>
                            <div class="col-4">
                                <label class="form-label" for="executionMethod">Execution Method</label>
                                <g:select class="form-select" name="executionMethod" from="${['Automated', 'Manual']}"
                                          noSelection="${['':'']}" value="${scenario.executionMethod}"
                                />
                            </div>
                        </div>
                        <div class="hstack gap-3 mb-3">
                            <div class="col-4">
                                <label class="form-label" for="platform">Platform</label>
                                <g:select class="form-select" name="platform" from="${['Android', 'iOS', 'Web']}"
                                          noSelection="${['':'']}" value="${scenario.platform}"
                                />
                            </div>
                            <div class="col-4">
                                <label class="form-label" for="type">Type</label>
                                <g:select class="form-select" name="type" from="${['API', 'UI']}"
                                          noSelection="${['':'']}" value="${scenario.type}"
                                />
                            </div>
                        </div>
                        <div class="col-4 mb-3">
                            <label class="form-label" for="environments">Environments</label>
                            <g:select class="form-select" name="environments" id="environments" from="${scenario.project.environments}"
                                      optionKey="id" optionValue="name" value="${scenario.environments?.id}"
                                      noSelection="${['':'No Environment...']}" multiple="true"
                            />
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="gherkin">Gherkin</label>
                            <g:textArea class="form-control" maxLength="2500" name="gherkin" value="${scenario.gherkin}"/>
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
