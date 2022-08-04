<div class="container-fluid">
    <div class="row">
        <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse" style="">
            <div class="sidebar-sticky pt-3">
                <ul class="nav flex-column">
                    <sec:ifAnyGranted roles="ROLE_READ_ONLY">
                        <g:if test="${params.projectId}">
                            <li class="nav-item">
                                <g:link class="nav-link text-capitalize text-wrap" uri="/project/home/${params.projectId}"><h1>${name} (${code})</h1></g:link>
                            </li>
                            <div class="dropdown-divider"></div>
                            <li class="nav-item">
                                <g:link class="nav-link" uri="/project/${params.projectId}/bugs">Bugs</g:link>
                            </li>
                            <li class="nav-item">
                                <g:link class="nav-link" uri="/project/${params.projectId}/releasePlans">Release Plans</g:link>
                            </li>
                            <li class="nav-item">
                                <g:link class="nav-link" uri="/project/${params.projectId}/scenarios">Scenarios</g:link>
                            </li>
                            <li class="nav-item">
                                <g:link class="nav-link" uri="/project/${params.projectId}/testCases">Test Cases</g:link>
                            </li>
                            <li class="nav-item">
                                <g:link class="nav-link" uri="/project/${params.projectId}/testGroups">Test Groups</g:link>
                            </li>
                        </g:if>
                        <g:else>
                            <li class="nav-item">
                                <g:link class="nav-link" elementId="projectsLink" controller="project" action="projects">Projects</g:link>
                            </li>
                        </g:else>
                    </sec:ifAnyGranted>
                </ul>
            </div>
        </nav>
    </div>
</div>