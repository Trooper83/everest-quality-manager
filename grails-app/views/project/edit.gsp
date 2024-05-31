<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${project}" var="entity"/>
            <g:form resource="${this.project}" method="PUT" useToken="true">
                <g:hiddenField name="version" value="${this.project?.version}" />
                <div class="card mt-3">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.edit.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <div class="col-4 required mb-3">
                            <label class="form-label fw-bold" for="code">Code</label>
                            <g:textField class="form-control" name="code" value="${project.code}" minLength="3" maxLength="5"></g:textField>
                        </div>
                        <div class="col-8 required mb-3">
                            <label class="form-label fw-bold" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${project.name}" maxLength="100"></g:textField>
                        </div>
                        <div class="col-10 mb-1" id="areas">
                            <label class="form-label fw-bold" for="area">Areas</label>
                            <div class="col-10 hstack gap-3">
                                <g:field class="form-control" maxLength="100" type="text" name="area" data-toggle="tooltip"
                                         trigger="manual" title="Area Name cannot be blank"/>
                                <g:field class="btn btn-light border" type="button" name="btnAddArea" value="Add"
                                         onclick="addTag('area')"/>
                            </div>
                        </div>
                        <div class="row mb-3" id="areaRow">
                            <div class="col-8">
                                <ul class="no-bullets list-inline">
                                    <g:each status="i" var="area" in="${project.areas}">
                                        <li class="list-inline-item" name="${area.name}">
                                            <div class="form-row ml-1">
                                                <p class="ml-1 badge text-bg-light border">${area.name}<svg xmlns="http://www.w3.org/2000/svg"
                                                        style="cursor:pointer" width="16" height="16" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16" onclick="removeTag(this,'area',${area.id})">
                                                    <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
                                                </svg>
                                                </p>
                                                <g:hiddenField name="areas[${i}].id" value="${area.id}"/>
                                                <input style="display: none;" type="text" id="areas[${i}].name" class="form-control-sm"
                                                       name="areas[${i}].name" value="${area.name}" data-test-id="tag-input"
                                                />
                                            </div>
                                        </li>
                                    </g:each>
                                </ul>
                            </div>
                        </div>
                        <div class="col-10 mb-1" id="environments">
                            <label class="form-label fw-bold" for="environment">Environments</label>
                            <div class="col-10 hstack gap-3">
                                <g:field class="form-control" maxLength="100" type="text" name="environment" data-toggle="tooltip"
                                         trigger="manual" title="Environment Name cannot be blank"/>
                                <g:field class="btn btn-light border" type="button" name="btnAddEnv" value="Add"
                                         onclick="addTag('environment')"/>
                            </div>
                        </div>
                        <div class="row mb-3" id="environmentRow">
                            <div class="col-8">
                                <ul class="no-bullets list-inline">
                                    <g:each status="i" var="env" in="${project.environments}">
                                        <li class="list-inline-item" name="${env.name}">
                                            <div class="form-row ml-1">
                                                <p class="ml-1 badge text-bg-light border">${env.name}<svg xmlns="http://www.w3.org/2000/svg" style="cursor:pointer"
                                                        width="16" height="16" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16" onclick="removeTag(this,'environment',${env.id})">
                                                    <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
                                                </svg>
                                                </p>
                                                <g:hiddenField name="environments[${i}].id" value="${env.id}"/>
                                                <input style="display: none;" type="text" id="environments[${i}].name" class="form-control-sm"
                                                       name="environments[${i}].name" value="${env.name}" data-test-id="tag-input"
                                                />
                                            </div>
                                        </li>
                                    </g:each>
                                </ul>
                            </div>
                        </div>
                        <div class="col-10 mb-1" id="platforms">
                            <label class="form-label fw-bold" for="platform">Platforms</label>
                            <div class="col-10 hstack gap-3">
                                <g:field class="form-control" maxLength="100" type="text" name="platform" data-toggle="tooltip"
                                         trigger="manual" title="Platform Name cannot be blank"/>
                                <g:field class="btn btn-light border" type="button" name="btnAddPlatform" value="Add"
                                         onclick="addTag('platform')"/>
                            </div>
                        </div>
                        <div class="row mb-3" id="platformRow">
                            <div class="col-8">
                                <ul class="no-bullets list-inline">
                                    <g:each status="i" var="platform" in="${project.platforms}">
                                        <li class="list-inline-item" name="${platform.name}">
                                            <div class="form-row ml-1">
                                                <p class="ml-1 badge text-bg-light border">${platform.name}<svg xmlns="http://www.w3.org/2000/svg" style="cursor:pointer"
                                                                                                           width="16" height="16" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16" onclick="removeTag(this,'platform',${platform.id})">
                                                    <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
                                                </svg>
                                                </p>
                                                <g:hiddenField name="platforms[${i}].id" value="${platform.id}"/>
                                                <input style="display: none;" type="text" id="platforms[${i}].name" class="form-control-sm"
                                                       name="platforms[${i}].name" value="${platform.name}" data-test-id="tag-input"
                                                />
                                            </div>
                                        </li>
                                    </g:each>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer">
                        <g:submitButton name="updateButton" data-test-id="edit-update-button" class="btn btn-primary mt-2" value="Update"/>
                    </div>
                </div>
            </g:form>
        </main>
    </div>
</div>
</div>
<asset:javascript src="projectItems.js"/>
</body>
</html>
