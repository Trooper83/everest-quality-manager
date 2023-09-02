<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'step.label', default: 'Step')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${step}" var="entity"/>
            <g:form resource="${this.step}" method="POST" uri="/project/${project.id}/step/save" useToken="true">
                <div class="card mt-3">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.create.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <g:hiddenField name="project" value="${project.id}"/>
                        <g:hiddenField name="isBuilderStep" value="true"/>
                        <div class="col-8 required mb-3">
                            <label class="form-label" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${step.name}" maxLength="255" autocomplete="off"></g:textField>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="act">Action</label>
                            <g:textArea class="form-control" maxLength="500" name="act" value="${step.act}"/>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="result">Result</label>
                            <g:textArea class="form-control" maxLength="500" name="result" value="${step.result}"/>
                        </div>
                    </div>
                </div>
                <div class="card mt-3 mb-5">
                    <div class="card-header">
                        <h1>Linked Steps</h1>
                    </div>
                    <div class="card-body">
                        <div class="row align-items-end mb-3">
                            <div class="col-2">
                                <label class="form-label" for="relation">Relationship</label>
                                <g:select class="form-select" name="relation"
                                          from="${['Is Child of', 'Is Parent of', 'Is Sibling of']}"
                                          noSelection="${['':'']}" data-toggle="tooltip"
                                          trigger="manual" title="Field cannot be blank"
                                />
                            </div>
                            <div class="col-6">
                                <label class="form-label" for="linkedSteps">Step</label>
                                <g:textField class="form-control" name="linkedSteps" type="text" placeholder="Name"
                                             autocomplete="off" data-toggle="tooltip"
                                             trigger="manual" title="Field cannot be blank"/>
                                <ul class="search" id="search-results" style="position:absolute;"></ul>
                            </div>
                            <div class="col-1">
                                <g:field class="btn btn-light border" type="button" name="btnAddLink" value="Add"
                                         onclick="addLink(this)"/>
                            </div>
                        </div>
                        <div class="row mb-3" id="stepLinks">

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
<asset:javascript src="searchSteps.js"/>
</body>
</html>