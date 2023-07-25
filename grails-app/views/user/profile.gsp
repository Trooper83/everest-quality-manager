<html>
<head>
    <meta name="layout" content="main"/>
    <title>User Profile</title>
</head>
<body>
<div class="container">
    <main class="mt-3">
        <div class="row justify-content-center">
            <div class="col-lg-5 col-md-8">
                <g:render template="/shared/messagesTemplate" bean="${user}" var="entity"/>
            </div>
        </div>
        <div class="row justify-content-center">
            <div class="col-lg-5 col-md-8">
                <g:form action="updatePassword" method="POST" useToken="true">
                    <g:hiddenField name="version" value="${this.user?.version}"/>
                    <g:hiddenField name="id" value="${user.id}"/>
                    <div class="card mt-3">
                        <div class="card-header">
                            <h1>Edit Profile</h1>
                        </div>
                        <div class="card-body">
                            <div class="row justify-content-center">
                                    <div class="required mb-3">
                                        <label class="form-label" for="email">Email</label>
                                        <g:textField class="form-control" name="email" value="${user.email}"
                                                     disabled="disabled"/>
                                    </div>
                                    <div class="required mb-3">
                                        <label class="form-label" for="password">Password</label>
                                        <g:passwordField class="form-control" name="password"
                                                         autocomplete="new-password"
                                        />
                                    </div>
                                    <div class="required mb-3">
                                        <label class="form-label" for="confirmPassword">Confirm Password</label>
                                        <g:passwordField class="form-control" name="confirmPassword"/>
                                    </div>
                            </div>
                        </div>
                        <div class="card-footer">
                            <g:submitButton name="Update" data-test-id="edit-update-button" class="btn btn-primary"
                                            value="${message(code: 'default.button.update.label', default: 'Update')}"/>
                        </div>
                    </div>
                </g:form>
			</div>
		</div>
    </main>
</div>
</body>
</html>
