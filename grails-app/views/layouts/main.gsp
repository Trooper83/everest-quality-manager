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
    <a class="navbar-brand" href="/projects">Title of App</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" aria-expanded="false" style="height: 0.8px;" id="navbarContent">
        <ul class="nav navbar-nav ml-auto">
            <g:pageProperty name="page.nav"/>
        </ul>
    </div>
    <ul class="navbar-nav mr-auto">
        <sec:ifAnyGranted roles="ROLE_PROJECT_ADMIN">
        <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="adminDropDown" role="button" data-toggle="dropdown" aria-expanded="false">
                Admin
            </a>
            <div class="dropdown-menu bg-secondary" id="adminDropDownMenu">
                <h4 class="dropdown-header text-white">Project</h4>
                <g:link class="dropdown-item" controller="project" action="create">Create Project</g:link>
                <sec:ifAnyGranted roles="ROLE_APP_ADMIN">
                    <div class="dropdown-divider"></div>
                    <h4 class="dropdown-header text-white">User</h4>
                    <g:link class="dropdown-item" controller="user" action="create">Create User</g:link>
                    <g:link class="dropdown-item" controller="user" action="search">Search Users</g:link>
                </sec:ifAnyGranted>
            </div>
        </li>
        </sec:ifAnyGranted>
        <sec:ifAnyGranted roles="ROLE_READ_ONLY">
            <g:if test="${params.projectId}">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="domainDropDown" role="button" data-toggle="dropdown" aria-expanded="false">
                        Project
                    </a>
                    <div class="dropdown-menu bg-secondary" id="domainDropDownMenu">
                        <g:link class="dropdown-item" uri="/project/home/${params.projectId}">Project Home</g:link>
                        <g:link class="dropdown-item" elementId="projectsLink" controller="project" action="projects">Projects</g:link>
                        <div class="dropdown-divider"></div>
                        <g:link class="dropdown-item" uri="/project/${params.projectId}/bugs">Bugs</g:link>
                        <g:link class="dropdown-item" uri="/project/${params.projectId}/releasePlans">Release Plans</g:link>
                        <g:link class="dropdown-item" uri="/project/${params.projectId}/scenarios">Scenarios</g:link>
                        <g:link class="dropdown-item" uri="/project/${params.projectId}/testCases"> Test Cases</g:link>
                        <g:link class="dropdown-item" uri="/project/${params.projectId}/testGroups">Test Groups</g:link>
                    </div>
                </li>
            </g:if>
            <g:else>
                <li class="nav-item">
                    <g:link class="nav-link" elementId="projectsLink" controller="project" action="projects">Projects</g:link>
                </li>
            </g:else>
        </sec:ifAnyGranted>
        <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" id="profileDropDown" href="#" role="button" data-toggle="dropdown" aria-expanded="false">
                <asset:image src="icons/person-circle.svg" alt="User Profile" width="32" height="32"/>
            </a>
            <div class="dropdown-menu bg-secondary" aria-labelledby="profileDropDown">
                <g:link class="dropdown-item" elementId="profileLink" controller="user" action="profile">Profile</g:link>
                <g:form controller="logout" method="POST">
                    <button data-test-id="main-logout-button" class="btn btn-link dropdown-item">Logout</button>
                </g:form>
            </div>
        </li>
    </ul>
</nav>

<g:layoutBody/>

<div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>
<asset:javascript src="application.js"/>
<asset:javascript src="popper.min.js"/>
</body>
</html>
