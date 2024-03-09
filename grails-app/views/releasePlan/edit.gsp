<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'releasePlan.label', default: 'ReleasePlan')}"/>
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate"
                  model="['name':releasePlan.project.name, 'code':releasePlan.project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${releasePlan}" var="entity"/>
            <g:form resource="${this.releasePlan}" method="PUT"
                    uri="/project/${releasePlan.project.id}/releasePlan/update/${releasePlan.id}" useToken="true">
                <g:hiddenField name="version" value="${this.releasePlan?.version}"/>
                <div class="card mt-3">
                    <div class="card-header">
                        <h1>
                            <g:message code="default.edit.label" args="[entityName]"/>
                        </h1>
                    </div>
                    <div class="card-body">
                        <div class="col-8 required mb-3">
                            <label class="form-label" for="name">Name</label>
                            <g:textField class="form-control" name="name" value="${releasePlan.name}"
                                         maxLength="100"></g:textField>
                        </div>
                        <div class="col-4 mb-3">
                            <label class="form-label" for="status">Status</label>
                            <g:select class="form-select" name="status" value="${releasePlan.status}"
                                      from="${['ToDo', 'Planning', 'In Progress', 'Released', 'Canceled']}"/>
                        </div>
                        <div class="col-6 date-picker mb-3">
                            <label class="form-label" style="display:block" for="plannedDate">Planned Date</label>
                            <g:datePicker name="plannedDate" default="none" precision="day"
                                          value="${releasePlan.plannedDate}"
                                          noSelection="['':'']" relativeYears="[0..1]"/>
                        </div>
                        <div class="col-6 date-picker mb-3">
                            <label class="form-label" style="display:block" for="releaseDate">Release Date</label>
                            <g:datePicker name="releaseDate" default="none" precision="day"
                                          value="${releasePlan.releaseDate}"
                                          noSelection="['':'']" relativeYears="[0..1]"/>
                        </div>
                        <div class="col-8 mb-3">
                            <label class="form-label" for="notes">Notes</label>
                            <g:textArea class="form-control" name="notes" value="${releasePlan.notes}"
                                  maxLength="1000"></g:textArea>
                        </div>
                    </div>
                    <div class="card-footer">
                        <g:submitButton name="update" class="btn btn-primary"
                                        value="${message(code: 'default.button.update.label', default: 'Update')}"/>
                    </div>
                </div>
            </g:form>
        </main>
    </div>
</div>
</body>
</html>
