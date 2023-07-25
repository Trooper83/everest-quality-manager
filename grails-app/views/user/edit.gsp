<html>
<head>
	<meta name="layout" content="main"/>
	<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
	<title><g:message code="default.edit.label" args="[entityName]" /></title>
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
			<div class="col-lg-5 col-md-8">
				<g:form controller="user" action="update" useToken="true">
					<g:hiddenField name="id" value="${user.id}" />
					<g:hiddenField name="version" value="${this.user?.version}"/>
					<div class="card mt-3 mb-5">
						<div class="card-header">
							<h1>
								<g:message code='default.edit.label' args='[entityName]'/>
							</h1>
						</div>
						<div class="card-body">
							<div class="required mb-3">
								<label class="form-label" for="email">Email</label>
								<g:textField class="form-control" name="email" value="${user.email}"
											 disabled="disabled"
								/>
							</div>
							<div class="required mb-3">
								<label class="form-label" for="password">Password</label>
								<g:passwordField class="form-control" name="password" value="${user.password}"/>
							</div>
							<div class="mt-3 mb-3">
								<h1>Status</h1>
							</div>
							<div class="form-check">
								<g:checkBox class="form-check-input" type="checkbox" name='enabled'
											value="${user.enabled}"
								/>
								<label class="form-check-label" for="enabled">Enabled</label>
							</div>
							<div class="form-check">
								<g:checkBox class="form-check-input" type="checkbox" name='passwordExpired'
											value="${user.passwordExpired}"
								/>
								<label class="form-check-label" for="passwordExpired">Password Expired</label>
							</div>
							<div class="form-check">
								<g:checkBox class="form-check-input" type="checkbox" name='accountLocked'
											value="${user.accountLocked}"
								/>
								<label class="form-check-label" for="accountLocked">Account Locked</label>
							</div>
							<div class="form-check">
								<g:checkBox class="form-check-input" type="checkbox" name='accountExpired'
											value="${user.accountExpired}"
								/>
								<label class="form-check-label" for="accountExpired">Account Expired</label>
							</div>
							<div class="mt-3 mb-3">
								<h1>Roles</h1>
							</div>
							<g:each var='role' in='${roleMap}'>
								<div class="form-check">
									<g:set var='roleName'
										   value='${uiPropertiesStrategy.getProperty(role.key, "authority")}'/>
									<g:checkBox class="form-check-input" type="checkbox" name='${roleName}'
												value="${role.value}"
									/>
									<label class="form-check-label">${roleName}</label>
								</div>
							</g:each>
						</div>
						<div class="card-footer">
							<g:submitButton name="update" class="btn btn-primary"
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
