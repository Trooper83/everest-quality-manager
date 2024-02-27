<nav id="sidebarMenu" class="col-md-3 col-lg-2 d-none d-md-block bg-light sidebar">
    <div class="sidebar-sticky">
        <ul class="nav flex-column">
            <sec:ifAnyGranted roles="ROLE_READ_ONLY">
                <g:if test="${params.projectId}">
                    <li class="nav-item">
                        <g:link class="nav-link text-capitalize text-wrap" uri="/project/home/${params.projectId}"><h1>${name} (${code})</h1></g:link>
                    </li>
                    <sec:ifAnyGranted roles="ROLE_BASIC">
                        <li class="text-center mt-2 mb-2">
                            <button id="dropdownCreateButton" type="button" class="btn btn-primary dropdown-toggle"
                                    data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Create</button>
                            <div class="dropdown-menu" aria-labelledby="dropdownCreateButton" id="dropdownCreateMenu">
                                <g:link class="dropdown-item"
                                        uri="/project/${params.projectId}/bug/create">Bug</g:link>
                                <g:link class="dropdown-item"
                                        uri="/project/${params.projectId}/releasePlan/create">Release Plan</g:link>
                                <g:link class="dropdown-item"
                                        uri="/project/${params.projectId}/scenario/create">Scenario</g:link>
                                <g:link class="dropdown-item"
                                        uri="/project/${params.projectId}/stepTemplate/create">Step Template</g:link>
                                <g:link class="dropdown-item"
                                        uri="/project/${params.projectId}/testCase/create">Test Case</g:link>
                                <g:link class="dropdown-item"
                                        uri="/project/${params.projectId}/testGroup/create">Test Group</g:link>
                            </div>
                        </li>
                    </sec:ifAnyGranted>
                    <div class="dropdown-divider"></div>
                    <li class="nav-item">
                        <g:link class="nav-link" uri="/project/${params.projectId}/automatedTests">Automated Tests</g:link>
                    </li>
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
                        <g:link class="nav-link" uri="/project/${params.projectId}/stepTemplates">Step Templates</g:link>
                    </li>
                    <li class="nav-item">
                        <g:link class="nav-link" uri="/project/${params.projectId}/testCases">Test Cases</g:link>
                    </li>
                    <li class="nav-item">
                        <g:link class="nav-link" uri="/project/${params.projectId}/testGroups">Test Groups</g:link>
                    </li>
                    <li class="nav-item">
                        <g:link class="nav-link" uri="/project/${params.projectId}/testRuns">Test Runs</g:link>
                    </li>
                    <div class="dropdown-divider"></div>
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