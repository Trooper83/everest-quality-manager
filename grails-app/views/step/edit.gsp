<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'step.label', default: 'Step')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':step.project.name, 'code':step.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${step}" var="entity"/>
            <g:form resource="${this.step}" method="PUT"
                    uri="/project/${step.project.id}/step/update/${step.id}" useToken="true">
                <g:hiddenField name="version" value="${this.step?.version}" />
                <div class="card mt-3 mb-5">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.edit.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <g:hiddenField name="project" value="${step.project.id}"/>
                        <div class="col-8 required mb-3">
                            <label class="form-label" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${step.name}" maxLength="100"></g:textField>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="act">Action</label>
                            <g:textArea class="form-control" name="act" value="${step.act}" maxLength="500"></g:textArea>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="result">Result</label>
                            <g:textArea class="form-control" name="result" value="${step.result}" maxLength="500"></g:textArea>
                        </div>
                    </div>
                </div>
                <div class="card mt-3 mb-5" id="relatedSteps">
                    <div class="card-header">
                        <h1>Related Steps</h1>
                    </div>
                    <div class="card-body">
                        <div class="mb-2" id="validate"></div>
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
                                <label class="form-label" for="search">Step</label>
                                <g:textField class="form-control" name="search" type="text" placeholder="Name"
                                             autocomplete="off" data-toggle="tooltip"
                                             trigger="manual" title="Field cannot be blank"/>
                                <ul class="search-results-menu col-6" id="search-results" style="position:absolute; z-index:999;"></ul>
                            </div>
                            <div class="col-1">
                                <g:field class="btn btn-light border" type="button" name="btnAdd" value="Add"
                                         onclick="addLinkItem(this)"/>
                            </div>
                        </div>
                        <div id="links">
                            <div class="row">
                                <p class="border-bottom">Parents</p>
                                <div class="row row-cols-md-3 row-cols-sm-2 mb-2 mt-3" style="min-height:2.5em;" id="parents">
                                    <g:each in="${linkedMap.parents}">
                                        <div class="col">
                                            <div class="card mb-2">
                                                <div class="card-body">
                                                    <svg xmlns="http://www.w3.org/2000/svg"
                                                         style="cursor:pointer" width="20" height="20" fill="currentColor"
                                                         class="bi bi-x position-absolute top-0 end-0 mt-1"
                                                         viewBox="0 0 20 20" onclick="removeLink(this, ${it.linkId})">
                                                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
                                                    </svg>
                                                    <p data-test-id="linkedItemName"><strong>Name: </strong>${it.linkedItem.name}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </g:each>
                                </div>
                            </div>
                            <div class="row">
                                <p class="border-bottom">Siblings</p>
                                <div class="row row-cols-md-3 row-cols-sm-2 mb-2 mt-3" style="min-height:2.5em;" id="siblings">
                                    <g:each in="${linkedMap.siblings}">
                                        <div class="col">
                                            <div class="card mb-2">
                                                <div class="card-body">
                                                    <svg xmlns="http://www.w3.org/2000/svg"
                                                         style="cursor:pointer" width="20" height="20" fill="currentColor"
                                                         class="bi bi-x position-absolute top-0 end-0 mt-1"
                                                         viewBox="0 0 20 20" onclick="removeLink(this, ${it.linkId})">
                                                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
                                                    </svg>
                                                    <p data-test-id="linkedItemName"><strong>Name: </strong>${it.linkedItem.name}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </g:each>
                                </div>
                            </div>
                            <div class="row">
                                <p class="border-bottom">Children</p>
                                <div class="row row-cols-md-3 row-cols-sm-2 mb-2 mt-3" style="min-height:2.5em;" id="children">
                                    <g:each in="${linkedMap.children}">
                                        <div class="col">
                                            <div class="card mb-2">
                                                <div class="card-body">
                                                    <svg xmlns="http://www.w3.org/2000/svg"
                                                         style="cursor:pointer" width="20" height="20" fill="currentColor"
                                                         class="bi bi-x position-absolute top-0 end-0 mt-1"
                                                         viewBox="0 0 20 20" onclick="removeLink(this, ${it.linkId})">
                                                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z"/>
                                                    </svg>
                                                    <p data-test-id="linkedItemName"><strong>Name: </strong>${it.linkedItem.name}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </g:each>
                                </div>
                            </div>
                        </div>
                        </div>
                    <div class="card-footer">
                        <g:submitButton data-test-id="edit-update-button" name="update" class="btn btn-primary" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                    </div>
                </div>
            </g:form>
        </main>
    </div>
    <g:render template="/shared/toastTemplate"/>
</div>
<asset:javascript src="linkItems.js"/>
</body>
</html>
