<html>
<head>
    <meta name="layout" content="main"/>
    <title>User Search</title>
</head>
<body>
<div class="container">
    <main class="mt-3">
        <div class="row justify-content-center">
            <div class="col-lg-8 col-md-9">
                <div class="col-lg-5 col-md-10">
                    <g:render template="/shared/messagesTemplate" bean="${user}" var="entity"/>
                </div>
                <g:form controller="user" action="search">
					<div class="card mt-3 mb-5">
						<div class="card-header">
							<h1>User Search</h1>
						</div>
						<div class="card-body">
							<div class="required mb-3 col-9">
								<label class="form-label" for="email">Email</label>
								<g:textField type='text' class="form-control" name='email' size='50' maxlength='255'
											 autocomplete='off' value='${email}'
								/>
							</div>
							<div class="row mt-3 mb-3">
								<div class="col-4">
								</div>
								<div class="col-2">
									True
								</div>
								<div class="col-2">
									False
								</div>
								<div class="col-2">
									Either
								</div>
							</div>
							<div class="row">
								<div class="col-4">
									Enabled:
								</div>
								<div class="col-2">
									<g:radio class="form-check-input" name="enabled" value="1"/>
								</div>
								<div class="col-2">
									<g:radio class="form-check-input" name="enabled" value="-1"/>
								</div>
								<div class="col-2">
									<g:radio class="form-check-input" name="enabled" value="0" checked="true"/>
								</div>
							</div>
							<div class="row">
								<div class="col-4">
									Account Expired:
								</div>
								<div class="col-2">
									<g:radio class="form-check-input" name="accountExpired" value="1"/>
								</div>
								<div class="col-2">
									<g:radio class="form-check-input" name="accountExpired" value="-1"/>
								</div>
								<div class="col-2">
									<g:radio class="form-check-input" name="accountExpired" value="0" checked="true"/>
								</div>
							</div>
							<div class="row">
								<div class="col-4">
									Account Locked:
								</div>
								<div class="col-2">
									<g:radio class="form-check-input" name="accountLocked" value="1"/>
								</div>
								<div class="col-2">
									<g:radio class="form-check-input" name="accountLocked" value="-1"/>
								</div>
								<div class="col-2">
									<g:radio class="form-check-input" name="accountLocked" value="0" checked="true"/>
								</div>
							</div>
							<div class="row">
								<div class="col-4">
									Password Expired:
								</div>
								<div class="col-2">
									<g:radio class="form-check-input" name="passwordExpired" value="1"/>
								</div>
								<div class="col-2">
									<g:radio class="form-check-input" name="passwordExpired" value="-1"/>
								</div>
								<div class="col-2">
									<g:radio class="form-check-input" name="passwordExpired" value="0" checked="true"/>
								</div>
							</div>
						</div>
						<div class="card-footer">
							<g:submitButton name="searchButton" class="btn btn-primary" value="Search"/>
						</div>
					</div>
                </g:form>
            </div>
            <g:if test='${searched}'>
				<div class="row mt-3">
					<div class="col">
						<table class="table table-light table-bordered" id="results">
							<thead class="thead-light">
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
									<td>
										<g:link action='edit' id='${user.id}'>${uiPropertiesStrategy.getProperty(user,
											'email')}
										</g:link>
									</td>
									<td>
										<s2ui:formatBoolean bean='${user}' name='enabled'/>
									</td>
									<td>
										<s2ui:formatBoolean bean='${user}' name='accountExpired'/>
									</td>
									<td>
										<s2ui:formatBoolean bean='${user}' name='accountLocked'/>
									</td>
									<td>
										<s2ui:formatBoolean bean='${user}' name='passwordExpired'/>
									</td>
								</tr>
							</g:each>
							</tbody>
						</table>
						<ul class="pagination mb-5">
							<g:pagination domain="user" total="${totalCount ?: 0}"/>
						</ul>
					</div>
				</div>
            </g:if>
        </div>
    </main>
</div>
<s2ui:ajaxSearch paramName='email'/>
</body>
</html>
