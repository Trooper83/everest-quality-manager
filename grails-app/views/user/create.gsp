<html>
<head>
	<meta name="layout" content="main"/>
	<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
<div id="create-user" class="content scaffold-create" role="main">
	<h1><g:message code='default.create.label' args='[entityName]'/></h1>
	<g:render template="/shared/messagesTemplate" bean="${user}" var="entity"/>
	<g:form controller="user" action="save" focus="email" useToken="true">
		<fieldset class="form">
			<div class="fieldcontain">
				<label for="email">Email</label>
				<input type="text" name="email" id="email"/>
			</div>
			<div class="fieldcontain">
				<label for="password">Password</label>
				<input type="password" name="password" id="password"/>
			</div>
			<div class="fieldcontain">
				<label for="enabled">Enabled</label>
				<g:checkBox name="enabled"/>
			</div>
			<div class="fieldcontain">
				<label for="accountExpired">Account Expired</label>
				<g:checkBox name="accountExpired"/>
			</div>
			<div class="fieldcontain">
				<label for="accountLocked">Account Locked</label>
				<g:checkBox name="accountLocked"/>
			</div>
			<div class="fieldcontain">
				<label for="passwordExpired">Password Expired</label>
				<g:checkBox name="passwordExpired"/>
			</div>
		</fieldset>
		<fieldset class="form">
			<div class="fieldcontain"><h3>Roles</h3></div>
			<g:each var='role' in='${authorityList}'>
				<div class="fieldcontain">
					<g:set var='authority' value='${uiPropertiesStrategy.getProperty(role, "authority")}'/>
					<label>${authority}</label>
					<g:checkBox name='${authority}'/>
				</div>
			</g:each>
		</fieldset>
		<fieldset class="buttons">
			<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
		</fieldset>
	</g:form>
</div>
</body>
</html>
