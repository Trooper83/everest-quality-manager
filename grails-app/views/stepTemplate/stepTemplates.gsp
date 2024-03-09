<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'stepTemplate.label', default: 'Step Template')}"/>
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <div class="row">
        <g:render template="/shared/sidebarTemplate" model="['name':project.name, 'code':project.code]"/>
        <main class="col-md-9 col-lg-10 ms-sm-auto mt-3">
            <g:render template="/shared/messagesTemplate" bean="${stepTemplate}" var="entity"/>
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:form class="mt-3" uri="/project/${project.id}/stepTemplates" params="['isSearch': 'true']" useToken="true">
                <div class="col-lg-6 hstack gap-3">
                    <g:textField class="form-control" name="searchTerm" autocomplete="off" placeholder="Step Template Name"
                                 value="${params.searchTerm}"/>
                    <button class="btn btn-primary" type="submit" id="searchButton">Search</button>
                    <g:link uri="/project/${project.id}/stepTemplates" elementId="resetLink">Reset</g:link>
                </div>
            </g:form>
            <div class="row mt-3">
                <div class="col">
                    <table class="table table-light table-bordered">
                        <thead class="thead-light">
                        <tr>
                            <g:columnSort domain="stepTemplate" projectId="${project.id}" property="name" title="Name"/>
                            <th>Action</th>
                            <th>Result</th>
                            <g:columnSort domain="stepTemplate" projectId="${project.id}" property="person" title="Created By"/>
                            <g:columnSort domain="stepTemplate" projectId="${project.id}" property="dateCreated" title="Created"/>
                            <g:columnSort domain="stepTemplate" projectId="${project.id}" property="lastUpdated" title="Updated"/>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each var="template" in="${stepTemplateList}">
                            <tr>
                                <td><g:link uri="/project/${project.id}/stepTemplate/show/${template.id}">${template.name}</g:link></td>
                                <td>${template.act}</td>
                                <td>${template.result}</td>
                                <td>${template.person.email}</td>
                                <td data-name="createdDateValue">${template.dateCreated}</td>
                                <td data-name="updatedDateValue">${template.lastUpdated}</td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <ul class="pagination mb-5">
                        <g:pagination domain="stepTemplate" projectId="${project.id}" total="${stepTemplateCount ?: 0}"/>
                    </ul>
                </div>
            </div>
        </main>
    </div>
</div>
<asset:javascript src="time.js"/>
</body>
</html>