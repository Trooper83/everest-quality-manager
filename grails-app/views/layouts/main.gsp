<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>

    <asset:stylesheet src="application.css"/>
    <g:layoutHead/>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark navbar-static-top" role="navigation">
    <a class="navbar-brand" href="/projects"><asset:image src="grails.svg" alt="Grails Logo"/></a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" aria-expanded="false" style="height: 0.8px;" id="navbarContent">
        <ul class="nav navbar-nav ml-auto">
            <g:pageProperty name="page.nav"/>
        </ul>
    </div>
    <div class="row justify-content-end">
        <div class="col">
            <sec:ifAnyGranted roles="ROLE_PROJECT_ADMIN">
                <div class="dropdown">
                    <button class="btn btn-secondary dropdown-toggle" type="button" id="adminButton" data-toggle="dropdown" aria-expanded="false">
                        Admin
                    </button>
                    <div class="dropdown-menu bg-secondary" id="adminDropDownMenu" aria-labelledby="adminDropDownMenu">
                        <h6 class="dropdown-header text-white">Project</h6>
                        <g:link class="dropdown-item" controller="project" action="create">Create Project</g:link>
                        <div class="dropdown-divider"></div>
                        <sec:ifAnyGranted roles="ROLE_APP_ADMIN">
                            <h6 class="dropdown-header text-white">User</h6>
                            <g:link class="dropdown-item" controller="user" action="create">Create User</g:link>
                            <g:link class="dropdown-item" controller="user" action="search">Search Users</g:link>
                        </sec:ifAnyGranted>
                    </div>
                </div>
            </sec:ifAnyGranted>
        </div>
    </div>
    <div class="container">
        <sec:ifLoggedIn>
            <g:link elementId="projectsLink" controller="project" action="projects">Projects</g:link>
        </sec:ifLoggedIn>
    </div>
    <div class="container">
        <sec:ifLoggedIn>
            <g:link elementId="profileLink" controller="user" action="profile">Profile</g:link>
        </sec:ifLoggedIn>
    </div>
    <div class="container">
        <sec:ifLoggedIn>
            <g:form controller="logout">
                <button data-test-id="main-logout-button">logout</button>
            </g:form>
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
            <g:link controller="login" action="auth" data-test-id="main-login-link">Login</g:link>
        </sec:ifNotLoggedIn>
    </div>
</nav>

<g:layoutBody/>

<div class="footer row" role="contentinfo">
    <div class="col">
        <a href="http://guides.grails.org" target="_blank">
            <asset:image src="advancedgrails.svg" alt="Grails Guides" class="float-left"/>
        </a>
        <strong class="centered"><a href="http://guides.grails.org" target="_blank">Grails Guides</a></strong>
        <p>Building your first Grails app? Looking to add security, or create a Single-Page-App? Check out the <a href="http://guides.grails.org" target="_blank">Grails Guides</a> for step-by-step tutorials.</p>

    </div>
    <div class="col">
        <a href="http://docs.grails.org" target="_blank">
            <asset:image src="documentation.svg" alt="Grails Documentation" class="float-left"/>
        </a>
        <strong class="centered"><a href="http://docs.grails.org" target="_blank">Documentation</a></strong>
        <p>Ready to dig in? You can find in-depth documentation for all the features of Grails in the <a href="http://docs.grails.org" target="_blank">User Guide</a>.</p>

    </div>

    <div class="col">
        <a href="https://grails-slack.cfapps.io" target="_blank">
            <asset:image src="slack.svg" alt="Grails Slack" class="float-left"/>
        </a>
        <strong class="centered"><a href="https://grails-slack.cfapps.io" target="_blank">Join the Community</a></strong>
        <p>Get feedback and share your experience with other Grails developers in the community <a href="https://grails-slack.cfapps.io" target="_blank">Slack channel</a>.</p>
    </div>
</div>


<div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>
<asset:javascript src="application.js"/>
<asset:javascript src="popper.min.js"/>
</body>
</html>
