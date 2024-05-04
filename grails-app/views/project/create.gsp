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
            <div class="card mt-3">
                <div class="card-header">
                    <h1>
                        <g:message code="default.create.label" args="[entityName]"/>
                    </h1>
                </div>
                <g:form class="col-12" resource="${this.project}" method="POST" useToken="true">
                    <div class="card-body">
                        <div class="col-4 required mb-3">
                            <label class="form-label" for="code">Code</label>
                            <g:textField class="form-control" name="code" value="${project.code}" minLength="3" maxLength="5"></g:textField>
                        </div>
                        <div class="form-group col-10 required mb-3">
                            <label class="form-label" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${project.name}" maxLength="100"></g:textField>
                        </div>
                        <div class="form-group col-8 mb-1" id="areas">
                            <div class="form-row align-items-end">
                                <label class="form-label" for="area">Areas</label>
                                <div class="hstack gap-3">
                                    <g:field class="form-control" maxLength="100" type="text" name="area" data-toggle="tooltip"
                                             trigger="manual" title="Area Name cannot be blank"/>
                                    <g:field class="btn btn-light border" type="button" name="btnAddArea" value="Add"
                                             onclick="addTag('area')"/>
                                </div>
                            </div>
                            <div class="row mb-3" id="areaRow">
                                <div class="col-8">
                                    <ul class="no-bullets list-inline"></ul>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-8 mb-1" id="environments">
                            <div class="form-row align-items-end">
                                <label class="form-label" for="environment">Environments</label>
                                <div class="hstack gap-3">
                                    <g:field class="form-control" maxLength="100" type="text" name="environment" data-toggle="tooltip"
                                             trigger="manual" title="Environment Name cannot be blank"/>
                                    <g:field class="btn btn-light border" type="button" name="btnAddEnv" value="Add"
                                             onclick="addTag('environment')"/>
                                </div>
                            </div>
                            <div class="row mb-3" id="environmentRow">
                                <div class="col-8">
                                    <ul class="no-bullets list-inline"></ul>
                                </div>
                            </div>
                        </div>
                        <div class="form-group col-8 mb-1" id="platforms">
                            <div class="form-row align-items-end">
                                <label class="form-label" for="platform">Platforms</label>
                                <div class="hstack gap-3">
                                    <g:field class="form-control" maxLength="100" type="text" name="platform" data-toggle="tooltip"
                                             trigger="manual" title="Platform Name cannot be blank"/>
                                    <g:field class="btn btn-light border" type="button" name="btnAddPlatform" value="Add"
                                             onclick="addTag('platform')"/>
                                </div>
                            </div>
                            <div class="row mb-3" id="platformRow">
                                <div class="col-8">
                                    <ul class="no-bullets list-inline"></ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer">
                        <g:submitButton name="create" class="btn btn-primary mt-2" value="Create"/>
                    </div>
                </g:form>
            </div>
        </div>
    </div>
</div>
<asset:javascript src="projectItems.js"/>
</body>
</html>
