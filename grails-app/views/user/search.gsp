<html>
<head>
	<meta name="layout" content="main"/>
	<title>User Search</title>
</head>
<body>
<div class="content" role="main">
	<h1>User Search</h1>
</div>
<div class="mt-5">
<g:form controller="user" action="search">
	<div class="row">
		<div class="col-2">Email:</div>
		<div class="col-6">
			<g:textField name='email' size='50' maxlength='255' autocomplete='off' value='${email}'/>
		</div>
	</div>
	<div class="row mt-3 mb-3">
		<div class="col-2">
		</div>
		<div class="col-1">
			True
		</div>
		<div class="col-1">
			False
		</div>
		<div class="col-1">
			Either
		</div>
	</div>
	<div class="row">
		<div class="col-2">
			Enabled:
		</div>
		<div class="col-1">
			<g:radio name="enabled" value="1"/>
		</div>
		<div class="col-1">
			<g:radio name="enabled" value="-1"/>
		</div>
		<div class="col-1">
			<g:radio name="enabled" value="0" checked="true"/>
		</div>
	</div>
	<div class="row">
		<div class="col-2">
			Account Expired:
		</div>
		<div class="col-1">
			<g:radio name="accountExpired" value="1"/>
		</div>
		<div class="col-1">
			<g:radio name="accountExpired" value="-1"/>
		</div>
		<div class="col-1">
			<g:radio name="accountExpired" value="0" checked="true"/>
		</div>
	</div>
	<div class="row">
		<div class="col-2">
			Account Locked:
		</div>
		<div class="col-1">
			<g:radio name="accountLocked" value="1"/>
		</div>
		<div class="col-1">
			<g:radio name="accountLocked" value="-1"/>
		</div>
		<div class="col-1">
			<g:radio name="accountLocked" value="0" checked="true"/>
		</div>
	</div>
	<div class="row">
		<div class="col-2">
			Password Expired:
		</div>
		<div class="col-1">
			<g:radio name="passwordExpired" value="1"/>
		</div>
		<div class="col-1">
			<g:radio name="passwordExpired" value="-1"/>
		</div>
		<div class="col-1">
			<g:radio name="passwordExpired" value="0" checked="true"/>
		</div>
	</div>
	<fieldset class="buttons pl-3 mt-5 mb-5">
	<button type="submit" class="btn btn-secondary" id="searchButton">Search</button>
	</fieldset>
</g:form>
</div>
	<g:if test='${searched}'>
	<div class="list">
		<table id="results">
			<thead>
			<tr>
				<s2ui:sortableColumn property='email' titleDefault='Email'/>
				<s2ui:sortableColumn property='enabled' titleDefault='Enabled'/>
				<s2ui:sortableColumn property='accountExpired' titleDefault='Account Expired'/>
				<s2ui:sortableColumn property='accountLocked' titleDefault='Account Locked'/>
				<s2ui:sortableColumn property='passwordExpired' titleDefault='Password Expired'/>
			</tr>
			</thead>
			<tbody>
			<g:each in='${results}' status='i' var='user'>
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td><g:link action='edit' id='${user.id}'>${uiPropertiesStrategy.getProperty(user, 'email')}</g:link></td>
					<td><s2ui:formatBoolean bean='${user}' name='enabled'/></td>
					<td><s2ui:formatBoolean bean='${user}' name='accountExpired'/></td>
					<td><s2ui:formatBoolean bean='${user}' name='accountLocked'/></td>
					<td><s2ui:formatBoolean bean='${user}' name='passwordExpired'/></td>
				</tr>
			</g:each>
			</tbody>
		</table>
	</div>
		<div class="pagination">
			<g:paginate total="${totalCount ?: 0}"/>
		</div>
	</g:if>
<s2ui:ajaxSearch paramName='email'/>
</body>
</html>
