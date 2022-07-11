<div class="row justify-content-end mt-3">
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
    </div>
</div>