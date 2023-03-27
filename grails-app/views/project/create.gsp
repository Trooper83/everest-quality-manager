<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}"/>
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
<div id="create-project" class="container" role="main">
    <div class="row mt-3">
        <div class="col">
            <g:render template="/shared/messagesTemplate" bean="${project}" var="entity"/>
        </div>
    </div>
    <div class="row justify-content-center">
        <div class="col-8">
            <div class="card mt-5">
                <div class="card-header">
                    <h1>
                        <g:message code="default.create.label" args="[entityName]"/>
                    </h1>
                </div>
                <div class="card-body">
                    <g:form class="col-12" resource="${this.project}" method="POST" useToken="true">
                        <div class="form-group col-4 required">
                            <label class="control-label" for="code">Code</label>
                            <g:textField class="form-control" name="code" value="${project.code}" maxLength="3"></g:textField>
                        </div>
                        <div class="form-group col-10 required">
                            <label class="control-label" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${project.name}" maxLength="100"></g:textField>
                        </div>
                        <div class="form-group col-10" id="areas">
                            <div class="form-row align-items-end">
                                <div class="col-10">
                                    <label for="area">Areas</label>
                                    <g:field class="form-control" maxLength="100" type="text" name="area" data-toggle="tooltip"
                                             trigger="manual" title="Area Name cannot be blank"/>
                                </div>
                                <div class="col-2">
                                    <g:field class="btn btn-light border" type="button" name="btnAddArea" value="Add"
                                             onclick="addTag('area')"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12">
                                    <ul class="no-bullets list-inline"></ul>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-10" id="environments">
                            <div class="form-row align-items-end">
                                <div class="col-10">
                                    <label for="environment">Environments</label>
                                    <g:field class="form-control" maxLength="100" type="text" name="environment" data-toggle="tooltip"
                                             trigger="manual" title="Environment Name cannot be blank"/>
                                </div>
                                <div class="col">
                                    <g:field class="btn btn-light border" type="button" name="btnAddEnv" value="Add"
                                             onclick="addTag('environment')"/>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-12">
                                    <ul class="no-bullets list-inline"></ul>
                                </div>
                            </div>
                        </div>
                        <div class="col">
                            <div class="dropdown-divider"></div>
                            <g:submitButton name="create" class="btn btn-primary mt-2" value="Create"/>
                        </div>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
</div>
<asset:javascript src="createProjectItems.js"/>
<asset:javascript src="popper.min.js"/>
</body>
</html>
