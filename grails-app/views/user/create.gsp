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
		<f:all bean="user" />
		<div class="fieldcontain"><h3>Roles</h3></div>
		<g:each var='role' in='${authorityList}'>
			<div class="fieldcontain">
				<g:set var='authority' value='${uiPropertiesStrategy.getProperty(role, "authority")}'/>
				<label>${authority}</label>
				<g:checkBox name='${authority}'/>
			</div>
		</g:each>
		<fieldset class="buttons">
			<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
		</fieldset>
	</g:form>
</div>
</body>
</html>
