<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: "testCase.label", default: "TestCase")}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-testCase" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: "/")}" data-test-id="create-home-link"><g:message code="default.home.label"/></a></li>
                <li>
                    <g:link class="list" action="index" data-test-id="create-list-link"><g:message code="default.list.label" args="[entityName]" /></g:link>
                </li>
            </ul>
        </div>
        <div id="create-testCase" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.testCase}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.testCase}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form controller="testCase" action="save" method="POST">
                <fieldset class="form">
                    <input type="hidden" name="creator" value="<sec:username/>"/>
                    <f:all bean="testCase" except="bugs, steps, creator"/>
                </fieldset>
                <fieldset>
                    <table class="table">
                        <thead>
                            <tr>
                                <th></th>
                                <th>Action</th>
                                <th>Result</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody id="stepsTableContent">
                        </tbody>
                    </table>
                    <input id="btnAddRow" type="button" value="Add" onclick="addEntryRow()" />
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: "default.button.create.label", default: "Create")}" />
                </fieldset>
            </g:form>
        </div>

        <asset:javascript src="step.js"/>
    </body>
</html>
