<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'testCycle.label', default: 'TestCycle')}"/>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<g:render template="/shared/sidebarTemplate" model="['name':testCycle.releasePlan.project.name, 'code':testCycle.releasePlan.project.code]"/>
<a href="#show-testCycle" class="skip" tabindex="-1">
    <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
</a>
<div id="show-testCycle" class="content scaffold-show col-md-9 ml-sm-auto col-lg-10 px-md-4" role="main">
    <h1>
        <g:message code="default.show.label" args="[entityName]"/>
    </h1>
    <g:render template="/shared/messagesTemplate" bean="${testCycle}" var="entity"/>
    <ol class="property-list testCycle">
        <li class="fieldcontain">
            <span id="releasePlan-label" class="property-label">Release Plan</span>
            <g:link class="property-value" elementId="releasePlan"
                    uri="/project/${this.testCycle.releasePlan.project.id}/releasePlan/show/${testCycle.releasePlan.id}">
                ${testCycle.releasePlan.name}</g:link>
        </li>
        <li class="fieldcontain">
            <span id="name-label" class="property-label">Name</span>
            <div class="property-value" id="name">${testCycle.name}</div>
        </li>
        <li class="fieldcontain">
            <span id="environ-label" class="property-label">Environment</span>
            <div class="property-value" id="environment">${testCycle.environ?.name}</div>
        </li>
        <li class="fieldcontain">
            <span id="platform-label" class="property-label">Platform</span>
            <div class="property-value" id="platform">${testCycle.platform}</div>
        </li>
        <sec:ifAnyGranted roles="ROLE_BASIC">
            <button id="addTestsBtn" type="button" class="btn btn-primary" data-toggle="modal" data-target="#testsModal">
                Add Tests
            </button>
        </sec:ifAnyGranted>
    </ol>
    <table>
        <thead>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Result</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <g:each var="iteration" in="${testCycle.testIterations}">
            <tr>
                <td><g:link uri="/project/${testCycle.releasePlan.project.id}/testIteration/show/${iteration.id}">${iteration.id}</g:link></td>
                <td>${iteration.name}</td>
                <td>${iteration.result}</td>
                <td><g:link uri="/project/${testCycle.releasePlan.project.id}/testIteration/execute/${iteration.id}">Execute</g:link></td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${testCycle.testIterations.size() ?: 0}"/>
    </div>
</div>
<div class="modal fade" id="testsModal" tabindex="-1" aria-labelledby="testsModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="testsModalLabel">Add Tests</h5>
                <button data-test-id="modal-close-button" type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form name="addTests" action="/testCycle/addTests?testCycleId=${testCycle.id}" method="POST" resource="${this.testCycle}">
                    <fieldset class="form">
                        <div class="fieldcontain">
                            <label for="testGroups">Test Groups</label>
                            <g:select
                                    name="testGroups" from="${testGroups}"
                                    optionKey="id" optionValue="name" multiple="true"
                            />
                        </div>
                    </fieldset>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal"
                                data-test-id="modal-cancel-button">Cancel
                        </button>
                        <g:submitButton data-test-id="modal-submit-button" class="btn btn-primary" name="submit" value="Add Tests"/>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<asset:javascript src="jquery-3.3.1.min.js"/>
<script>
        $(document).ready(function() {
            $('#testsModal').on('hidden.bs.modal', function () {
                $('#testsModal form')[0].reset();
            });
        });
    </script>
</body>
</html>
