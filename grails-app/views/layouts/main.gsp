<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <asset:link rel="shortcut icon" href="icons/favicon.ico" type="image/x-icon"/>
    <title>
        <g:layoutTitle default="Everest"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <asset:stylesheet src="application.css"/>
    <g:layoutHead/>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">
    <a class="navbar-brand ms-3" href="/projects">Everest</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent"
            aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse justify-content-end me-3" aria-expanded="false" style="height: 0.8px;" id="navbarContent">
        <ul class="navbar-nav">
            <sec:ifAnyGranted roles="ROLE_PROJECT_ADMIN">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="adminDropDown" role="button" data-bs-toggle="dropdown"
                       aria-expanded="false">Admin
                    </a>
                    <div class="dropdown-menu" id="adminDropDownMenu">
                        <h4 class="dropdown-header">Project</h4>
                        <g:link class="dropdown-item" controller="project" action="create">Create Project</g:link>
                        <sec:ifAnyGranted roles="ROLE_APP_ADMIN">
                            <div class="dropdown-divider"></div>
                            <h4 class="dropdown-header">User</h4>
                            <g:link class="dropdown-item" controller="user" action="create">Create User</g:link>
                            <g:link class="dropdown-item" controller="user" action="search">Search Users</g:link>
                        </sec:ifAnyGranted>
                    </div>
                </li>
            </sec:ifAnyGranted>
            <li class="nav-item">
            <li class="nav-item">
                <g:link class="nav-link" elementId="projectsLink" controller="project" action="projects">Projects</g:link>
            </li>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" id="profileDropDown" href="#" role="button" data-bs-toggle="dropdown"
                   aria-expanded="false">
                    <asset:image src="icons/person-circle.svg" alt="User Profile" width="32" height="32"/>
                </a>
                <div class="dropdown-menu dropdown-menu-end" aria-labelledby="profileDropDown">
                    <g:link class="dropdown-item" elementId="profileLink" controller="user" action="profile">Profile</g:link>
                    <g:form controller="logout" method="POST">
                        <button data-test-id="main-logout-button" class="btn btn-link dropdown-item">Logout</button>
                    </g:form>
                    <div class="dropdown-divider"></div>
                    <p class="dropdown-item">version: <g:meta name="info.app.version"/></p>
                </div>
            </li>
        </ul>
    </div>
</nav>

<g:layoutBody/>
<asset:javascript src="application.js"/>
</body>
</html>
