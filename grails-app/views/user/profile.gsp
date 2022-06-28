<html>
<head>
	<meta name="layout" content="main"/>
	<title>Profile</title>
</head>
<body>
<h3>Edit Profile</h3>
<g:render template="/shared/messagesTemplate" bean="${user}" var="entity"/>
<g:form action="updatePassword" method="POST" useToken="true">
	<g:hiddenField name="version" value="${this.user?.version}" />
	<g:hiddenField name="id" value="${user.id}" />
	<fieldset class="form">
		<div class="fieldcontain">
			<label for="email">Email</label>
			<g:textField name="email" value="${user.email}" disabled="disabled"/>
		</div>
		<div class="fieldcontain">
			<label for="password">Password</label>
			<g:passwordField name="password"/>
		</div>
		<div class="fieldcontain">
			<label for="confirmPassword">Confirm Password</label>
			<g:passwordField name="confirmPassword"/>
		</div>
	</fieldset>
	<fieldset class="buttons">
		<input class="save" type="submit" id="updateButton" value="${message(code: 'default.button.update.label', default: 'Update')}" />
	</fieldset>
	<g:checkBox name="enabled" value="${user.enabled}" hidden="hidden"/>
	<g:checkBox name="accountExpired" value="${user.accountExpired}" hidden="hidden"/>
	<g:checkBox name="accountLocked" value="${user.accountLocked}" hidden="hidden"/>
	<g:checkBox name="passwordExpired" value="${user.passwordExpired}" hidden="hidden"/>
	<g:each var='entry' in='${roleMap}'>
		<g:set var='roleName' value='${uiPropertiesStrategy.getProperty(entry.key, "authority")}'/>
		<div>
			<g:checkBox name='${roleName}' value='${entry.value}' hidden="hidden"/>
		</div>
	</g:each>
</g:form>
</body>
</html>
