<div class="nav" role="navigation">
    <ul>
        <li>
            <a class="home" href="${createLink(uri: '/')}" data-test-id="index-home-link">
                <g:message code="default.home.label"/>
            </a>
        </li>
        <sec:ifAnyGranted roles="ROLE_BASIC">
            <li>
                <g:link class="create" action="create" data-test-id="index-create-link">
                    <g:message code="default.new.label" args="[entityName]" />
                </g:link>
            </li>
        </sec:ifAnyGranted>
    </ul>
</div>