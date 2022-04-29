<div class="container">
    <div class="row justify-content-end">
        <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
            <div class="btn-group mr-2" role="group" aria-label="First group">
                <sec:ifAnyGranted roles="ROLE_BASIC">
                    <g:if test="${params.projectId}">
                        <div class="dropdown" id="createMenu">
                            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                Create
                            </button>
                            <div class="dropdown-menu bg-secondary" aria-labelledby="dropdownMenuButton">
                                <g:link class="dropdown-item" uri="/project/${params.projectId}/bug/create">Bug</g:link>
                                <g:link class="dropdown-item" uri="/project/${params.projectId}/releasePlan/create">Release Plan</g:link>
                                <g:link class="dropdown-item" uri="/project/${params.projectId}/scenario/create">Scenario</g:link>
                                <g:link class="dropdown-item" uri="/project/${params.projectId}/testCase/create">Test Case</g:link>
                                <g:link class="dropdown-item" uri="/project/${params.projectId}/testGroup/create">Test Group</g:link>
                            </div>
                        </div>
                    </g:if>
                </sec:ifAnyGranted>
            </div>
            <div class="btn-group mr-2" role="group" aria-label="Second group">
                <sec:ifAnyGranted roles="ROLE_READ_ONLY">
                    <div class="dropdown" id="listsMenu">
                        <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownListButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Project
                        </button>
                        <div class="dropdown-menu bg-secondary" aria-labelledby="navbarDropdown">
                            <g:if test="${params.projectId}">
                                <g:link class="dropdown-item" uri="/project/home/${params.projectId}">Project Home</g:link>
                                <div class="dropdown-divider"></div>
                                <g:link class="dropdown-item" uri="/project/${params.projectId}/bugs">Bugs</g:link>
                                <g:link class="dropdown-item" uri="/project/${params.projectId}/releasePlans">Release Plans</g:link>
                                <g:link class="dropdown-item" uri="/project/${params.projectId}/scenarios">Scenarios</g:link>
                                <g:link class="dropdown-item" uri="/project/${params.projectId}/testCases"> Test Cases</g:link>
                                <g:link class="dropdown-item" uri="/project/${params.projectId}/testGroups">Test Groups</g:link>
                            </g:if>
                        </div>
                    </div>
                </sec:ifAnyGranted>
            </div>
        </div>
    </div>
</div>