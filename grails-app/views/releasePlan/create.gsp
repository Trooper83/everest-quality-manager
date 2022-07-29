<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'releasePlan.label', default: 'ReleasePlan')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
    <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <a href="#create-releasePlan" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div id="create-releasePlan" class="content scaffold-create col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:render template="/shared/messagesTemplate" bean="${releasePlan}" var="entity"/>
            <g:form resource="${this.releasePlan}" method="POST" uri="/project/${project.id}/releasePlan/save">
                <fieldset class="form">
                    <g:hiddenField name="project" value="${project.id}"/>
                    <div class="fieldcontain">
                        <label for="name">Name
                            <span class="required-indicator">*</span>
                        </label>
                        <g:textField name="name" required="true"/>
                    </div>
                    <div class="fieldcontain">
                        <label for="status">Status</label>
                        <g:select name="status" from="${['ToDo', 'Planning', 'In Progress', 'Released']}"/>
                    </div>
                    <div class="fieldcontain">
                        <label for="plannedDate">Planned Date</label>
                        <g:datePicker name="plannedDate" value=" " precision="day"
                                      noSelection="['':'']" relativeYears="[0..1]"/>
                    </div>
                    <div class="fieldcontain">
                        <label for="releaseDate">Release Date</label>
                        <g:datePicker name="releaseDate" value=" " precision="day"
                                      noSelection="['':'']" relativeYears="[0..1]"/>
                    </div>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>
    <asset:javascript src="popper.min.js"/>
    </body>
</html>
