<html>
<head>
	<meta name="layout" content="main"/>
	<g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
	<title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div id="edit-user" class="content scaffold-edit" role="main">
	<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
	<g:render template="/shared/messagesTemplate" bean="${user}" var="entity"/>
	<g:form controller="user" action="update" focus="email" useToken="true">
		<g:hiddenField name="id" value="${user.id}" />
		<f:all bean="user" />
		<div class="fieldcontain"><h3>Roles</h3></div>
		<g:each var='entry' in='${roleMap}'>
			<div class="fieldcontain">
				<g:set var='roleName' value='${uiPropertiesStrategy.getProperty(entry.key, "authority")}'/>
				<label>${roleName}</label>
				<g:checkBox name='${roleName}' value='${entry.value}'/>
			</div>
		</g:each>
		<fieldset class="buttons">
			<input class="save" type="submit" data-test-id="edit-update-button" value="${message(code: 'default.button.update.label', default: 'Update')}" />
		</fieldset>
	</g:form>
</div>
</body>
</html>
