<sec:ifNotSwitched>
	<sec:ifAllGranted roles='${securityConfig.ui.switchUserRoleName}'>
	<g:set var='username' value='${uiPropertiesStrategy.getProperty(user, 'username')}'/>
	<g:if test='${username}'><g:set var='canRunAs' value='${true}'/></g:if>
	</sec:ifAllGranted>
</sec:ifNotSwitched>
<html>
<head>
	<meta name="layout" content="${layoutUi}"/>
	<s2ui:title messageCode='default.edit.label' entityNameMessageCode='user.label' entityNameDefault='User'/>
</head>
<body>
<h3><g:message code='default.edit.label' args='[entityName]'/></h3>
<s2ui:form type='update' beanName='user' focus='email' class='button-style' useToken='true'>
	<s2ui:tabs elementId='tabs' height='375' data='${tabData}'>
		<s2ui:tab name='userinfo' height='275'>
			<table>
				<tbody>
				<s2ui:textFieldRow name='email' labelCodeDefault='Email'/>
				<s2ui:passwordFieldRow name='password' labelCodeDefault='Password'/>
				<s2ui:checkboxRow name='enabled' labelCodeDefault='Enabled'/>
				<s2ui:checkboxRow name='accountExpired' labelCodeDefault='Account Expired'/>
				<s2ui:checkboxRow name='accountLocked' labelCodeDefault='Account Locked'/>
				<s2ui:checkboxRow name='passwordExpired' labelCodeDefault='Password Expired'/>
				</tbody>
			</table>
		</s2ui:tab>
		<s2ui:tab name='roles' height='275'>
		<g:each var='entry' in='${roleMap}'>
			<g:set var='roleName' value='${uiPropertiesStrategy.getProperty(entry.key, "authority")}'/>
			<div>
				<g:checkBox name='${roleName}' value='${entry.value}'/>
				<label>${roleName}</label>
			</div>
		</g:each>
		</s2ui:tab>
	</s2ui:tabs>
	<div style="float:left; margin-top: 10px;">
		<s2ui:submitButton/>
	</div>
</s2ui:form>
</body>
</html>
