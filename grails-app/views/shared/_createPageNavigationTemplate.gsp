<div class="nav" role="navigation">
    <ul>
        <li>
            <a class="home" href="${createLink(uri: '/')}" data-test-id="create-home-link">
                <g:message code="default.home.label"/>
            </a>
        </li>
        <li>
            <g:link class="list" action="index" data-test-id="create-list-link">
                <g:message code="default.list.label" args="[entityName]" />
            </g:link>
        </li>
    </ul>
</div>