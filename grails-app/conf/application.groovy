

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/projects'
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.everlution.Person'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.everlution.PersonRole'
grails.plugin.springsecurity.authority.className = 'com.everlution.Role'
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
	//Set h2-console to env specific for development
	//[pattern: '/h2-console/**',  access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

/*These should only be set for development environment h2-console
grails.plugin.springsecurity.http.authorizeRequests = [pattern: "/h2-console/**", access: ["permitAll"]]
grails.plugin.springsecurity.http.csrf = false
grails.plugin.springsecurity.http.headers.frameOptions = false
end block*/

grails.plugin.springsecurity.roleHierarchy = '''
   ROLE_APP_ADMIN > ROLE_ORG_ADMIN
   ROLE_ORG_ADMIN > ROLE_PROJECT_ADMIN
   ROLE_PROJECT_ADMIN > ROLE_BASIC
   ROLE_BASIC > ROLE_READ_ONLY
'''

