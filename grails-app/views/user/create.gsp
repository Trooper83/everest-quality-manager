<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
<div class="container">
    <main class="mt-3">
        <div class="row justify-content-center">
            <div class="col-lg-5 col-md-10">
                <g:render template="/shared/messagesTemplate" bean="${user}" var="entity"/>
            </div>
        </div>
        <div class="row justify-content-center">
            <div class="col-lg-6 col-md-8">
                <g:form controller="user" action="save" useToken="true" autocomplete="off">
                    <div class="card mt-3">
                        <div class="card-header">
                            <h1>
                                <g:message code='default.create.label' args='[entityName]'/>
                            </h1>
                        </div>
                        <div class="card-body">
                            <div class="col-8">
                                <div class="required mb-3">
                                    <label class="form-label" for="email">Email</label>
                                    <g:textField class="form-control" name="email" autocomplete="off"/>
                                </div>
                                <div class="required mb-3">
                                    <label class="form-label" for="password">Password</label>
                                    <g:passwordField class="form-control" name="password"
                                                     autocomplete="new-password"
                                    />
                                </div>
                            </div>
                            <div class="mt-3 mb-3">
                                <h1>Status</h1>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="" id="enabled">
                                <label class="form-check-label" for="enabled">Enabled</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="" id="passwordExpired">
                                <label class="form-check-label" for="passwordExpired">Password Expired</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="" id="accountLocked">
                                <label class="form-check-label" for="accountLocked">Account Locked</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="" id="accountExpired">
                                <label class="form-check-label" for="accountExpired">Account Expired</label>
                            </div>
                            <div class="mt-3 mb-3">
                                <h1>Roles</h1>
                            </div>
                            <g:each var='role' in='${authorityList}'>
                                <div class="form-check">
                                    <g:set var='authority'
                                           value='${uiPropertiesStrategy.getProperty(role, "authority")}'/>
                                    <g:checkBox class="form-check-input" type="checkbox" name='${authority}'/>
                                    <label class="form-check-label">${authority}</label>
                                </div>
                            </g:each>
                        </div>
                        <div class="card-footer">
                            <g:submitButton name="create" class="btn btn-primary"
                                            value="${message(code: 'default.button.create.label', default: 'Create')}"/>
                        </div>
                    </div>
                </g:form>
            </div>
        </div>
    </main>
</div>
</body>
</html>
