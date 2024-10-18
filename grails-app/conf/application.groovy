

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/projects'
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.manager.quality.everest.domains.Person'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.manager.quality.everest.domains.PersonRole'
grails.plugin.springsecurity.authority.className = 'com.manager.quality.everest.domains.Role'
grails.plugin.springsecurity.userLookup.usernamePropertyName = 'email'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],
	[pattern: '/**/favicon.png', access: ['permitAll']],
	[pattern: '/user/create', 	 access: ['ROLE_APP_ADMIN']],
	[pattern: '/user/edit', 	 access: ['ROLE_APP_ADMIN']],
	[pattern: '/user/search', 	 access: ['ROLE_APP_ADMIN']],
	[pattern: '/user/save', 	 access: ['ROLE_APP_ADMIN']],
	[pattern: '/user/update', 	 access: ['ROLE_APP_ADMIN']],
	//Set h2-console to env specific for development
	//[pattern: '/h2-console/**',  access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**/favicon.png', filters: 'none'],
	[pattern: '/api/**', filters: 'JOINED_FILTERS, -anonymousAuthenticationFilter, -exceptionTranslationFilter,' +
			'-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'],
	[pattern: '/**', filters: 'JOINED_FILTERS, -restTokenValidationFilter,-restExceptionTranslationFilter']
]

/*
//These should only be set for development environment h2-console
grails.plugin.springsecurity.http.authorizeRequests = [pattern: "/h2-console/**", access: ["permitAll"]]
grails.plugin.springsecurity.http.csrf = false
grails.plugin.springsecurity.http.headers.frameOptions = false
//end block
*/

grails.plugin.springsecurity.roleHierarchy = '''
   ROLE_APP_ADMIN > ROLE_PROJECT_ADMIN
   ROLE_PROJECT_ADMIN > ROLE_BASIC
   ROLE_BASIC > ROLE_READ_ONLY
'''

//grails spring security rest plugin
grails.plugin.springsecurity.rest.token.validation.active = false
grails.plugin.springsecurity.rest.login.active = true
grails.plugin.springsecurity.rest.login.endpointUrl = '/api/login'
grails.plugin.springsecurity.rest.login.failureStatusCode = 401
grails.plugin.springsecurity.rest.login.useJsonCredentials = true
grails.plugin.springsecurity.rest.login.usernamePropertyName = 'username'
grails.plugin.springsecurity.rest.login.passwordPropertyName = 'password'
grails.plugin.springsecurity.rest.login.useRequestParamsCredentials = false
grails.plugin.springsecurity.rest.token.storage.jwt.useSignedJwt = true
grails.plugin.springsecurity.rest.token.storage.jwt.secret = 'i80Nm41D3nledZ3PpE11n6nR0ZzybL1nD6uarD1an3dGuy'
grails.plugin.springsecurity.rest.token.storage.jwt.expiration = 3600