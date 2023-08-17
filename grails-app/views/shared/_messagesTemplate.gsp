<g:if test="${flash.message}">
    <div class="alert alert-primary" role="alert">${flash.message}</div>
</g:if>
<g:if test="${flash.error}">
    <div class="alert alert-danger" role="alert">${flash.error}</div>
</g:if>
<g:hasErrors bean="${this.entity}">
    <g:eachError bean="${this.entity}" var="error">
        <div class="alert alert-danger" role="alert" <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></div>
    </g:eachError>
</g:hasErrors>