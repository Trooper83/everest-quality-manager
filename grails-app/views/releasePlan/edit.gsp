<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'releasePlan.label', default: 'ReleasePlan')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
    <g:render template="/shared/sidebarTemplate" model="['name':releasePlan.project.name, 'code':releasePlan.project.code]"/>
        <a href="#edit-releasePlan" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="edit-releasePlan" class="content scaffold-edit col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${releasePlan}" var="entity"/>
            <g:form resource="${this.releasePlan}" method="PUT" uri="/project/${releasePlan.project.id}/releasePlan/update/${releasePlan.id}">
                <g:hiddenField name="version" value="${this.releasePlan?.version}" />
                <fieldset class="form">
                    <div class="fieldcontain">
                        <label for="name">Name
                            <span class="required-indicator">*</span>
                        </label>
                        <g:textField name="name" required="true" value="${releasePlan.name}"/>
                    </div>
                    <div class="fieldcontain">
                        <label for="status">Status</label>
                        <g:select name="status" value="${releasePlan.status}"
                                  from="${['ToDo', 'Planning', 'In Progress', 'Released']}"
                        />
                    </div>
                    <div class="fieldcontain">
                        <label for="plannedDate">Planned Date</label>
                        <g:datePicker name="plannedDate" value="${releasePlan.plannedDate}" precision="day"
                                      noSelection="['':'']" relativeYears="[0..1]"/>
                    </div>
                    <div class="fieldcontain">
                        <label for="releaseDate">Release Date</label>
                        <g:datePicker name="releaseDate" value="${releasePlan.releaseDate}" precision="day"
                                      noSelection="['':'']" relativeYears="[0..1]"/>
                    </div>
                </fieldset>
                <fieldset class="buttons">
                    <input class="save" type="submit" data-test-id="edit-update-button" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
    <asset:javascript src="popper.min.js"/>
    </body>
</html>
