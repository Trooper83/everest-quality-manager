<g:if test="${flash.message}">
    <div class="alert alert-primary" role="alert">${flash.message}</div>
</g:if>
<g:if test="${flash.error}">
    <ul class="alert alert-danger" role="alert">${flash.error}</ul>
</g:if>
<g:hasErrors bean="${this.entity}">
    <ul class="alert alert danger" role="alert">
        <g:eachError bean="${this.entity}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
        </g:eachError>
    </ul>
</g:hasErrors>