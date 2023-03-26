<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div class="container-fluid">
    <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
    <div role="main" class="container">
        <div class="row justify-content-center">
            <div class="col-8">
                <div class="card mt-5">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.edit.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <g:render template="/shared/messagesTemplate" bean="${project}" var="entity"/>
                        <g:form class="col-12" resource="${this.project}" method="PUT" useToken="true">
                            <g:hiddenField name="version" value="${this.project?.version}" />
                            <div class="form-group col-4">
                                <label for="code">Code</label>
                                <g:textField class="form-control" name="code" value="${project.code}" maxLength="3"></g:textField>
                            </div>
                            <div class="form-group col-10">
                                <label for="name">Name</label>
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
                                                 onclick="addAreaTag()"/>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-12">
                                        <ul class="no-bullets list-inline">
                                            <g:each status="i" var="area" in="${project.areas}">
                                                <li class="list-inline-item" name="${area.name}">
                                                    <div class="form-row ml-1">
                                                        <h3>
                                                            <p class="font-weight-normal badge badge-light border border-secondary border-rounded">${area.name}
                                                                <svg xmlns="http://www.w3.org/2000/svg" style="cursor:pointer" width="12" height="12" fill="currentColor" class="bi bi-pencil" viewBox="0 0 20 20" onclick="displayAreaOptions(this, ${area.id})">
                                                                    <path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/>
                                                                </svg>
                                                            </p>
                                                        </h3>
                                                        <g:hiddenField name="areas[${i}].id" value="${area.id}"/>
                                                        <input style="display: none;" type="text" id="areas[${i}].name" class="form-control-sm"
                                                               name="areas[${i}].name" value="${area.name}" data-test-id="tag-input"
                                                               data-toggle="tooltip" trigger="manual" title="Area Name cannot be blank"
                                                        />
                                                    </div>
                                                </li>
                                            </g:each>
                                        </ul>
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
                                                 onclick="addEnvTag()"/>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-12">
                                        <ul class="no-bullets list-inline">
                                            <g:each status="i" var="env" in="${project.environments}">
                                                <li class="list-inline-item" name="${env.name}">
                                                    <div class="form-row ml-1">
                                                        <h3>
                                                            <p class="font-weight-normal badge badge-light border border-secondary border-rounded">${env.name}
                                                                <svg xmlns="http://www.w3.org/2000/svg" style="cursor:pointer" width="12" height="12" fill="currentColor" class="bi bi-pencil" viewBox="0 0 20 20" onclick="displayEnvOptions(this, ${env.id})">
                                                                    <path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168l10-10zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207 11.207 2.5zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293l6.5-6.5zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325z"/>
                                                                </svg>
                                                            </p>
                                                        </h3>
                                                        <g:hiddenField name="environments[${i}].id" value="${env.id}"/>
                                                        <input style="display: none;" type="text" id="environments[${i}].name" class="form-control-sm"
                                                               name="environments[${i}].name" value="${env.name}" data-test-id="tag-input"
                                                               data-toggle="tooltip" trigger="manual" title="Environment Name cannot be blank"
                                                        />
                                                    </div>
                                                </li>
                                            </g:each>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="dropdown-divider"></div>
                                <g:submitButton name="updateButton" data-test-id="edit-update-button" class="btn btn-primary mt-2" value="Update"/>
                            </div>
                        </g:form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<asset:javascript src="editProjectArea.js"/>
<asset:javascript src="editProjectEnvironment.js"/>
<asset:javascript src="popper.min.js"/>
</body>
</html>
