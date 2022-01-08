<div class="nav" role="navigation">
    <ul>
        <li>
            <a class="home" data-test-id="edit-home-link" href="${createLink(uri: '/')}">
                <g:message code="default.home.label"/>
            </a>
        </li>
        <li>
            <g:link class="list" action="index" data-test-id="edit-list-link">
                <g:message code="default.list.label" args="[entityName]" />
            </g:link>
        </li>
    </ul>
</div>