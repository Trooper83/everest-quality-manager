<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'releasePlan.label', default: 'ReleasePlan')}"/>
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${releasePlan}" var="entity"/>
            <g:form resource="${this.releasePlan}" method="POST" uri="/project/${project.id}/releasePlan/save"
                    useToken="true">
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
                            <g:textField class="form-control" name="name" value="${releasePlan.name}"
                                         maxLength="100"></g:textField>
                        </div>
                        <div class="col-4 mb-3">
                            <label class="form-label" for="status">Status</label>
                            <g:select class="form-select" name="status"
                                      from="${['ToDo', 'Planning', 'In Progress', 'Released']}"/>
                        </div>
                        <div class="col-6 date-picker mb-3">
                            <label class="form-label" style="display:block" for="plannedDate">Planned Date</label>
                            <g:datePicker name="plannedDate" default="none" precision="day"
                                          noSelection="['':'']" relativeYears="[0..1]"/>
                        </div>
                        <div class="col-6 date-picker mb-3">
                            <label class="form-label" style="display:block" for="releaseDate">Release Date</label>
                            <g:datePicker name="releaseDate" default="none" precision="day"
                                          noSelection="['':'']" relativeYears="[0..1]"/>
                        </div>
                    </div>
                    <div class="card-footer">
                        <g:submitButton name="create" class="btn btn-primary"
                                        value="${message(code: 'default.button.create.label', default: 'Create')}"/>
                    </div>
                </div>
            </g:form>
        </main>
    </div>
</div>
</body>
</html>
