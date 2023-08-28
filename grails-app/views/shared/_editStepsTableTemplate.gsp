<g:each status="i" var="step" in="${entity.steps}">
    <div class="row align-items-center mt-3">
        <g:hiddenField name="stepsIndex[${i}]" class="iHidden" />
        <div class="col-5">
            <g:textArea class="form-control" type="text" name="steps[${i}].act" value="${step.act}" />
        </div>
        <div class="col-5">
            <g:textArea class="form-control" type="text" name="steps[${i}].result" value="${step.result}" />
        </div>
        <div class="col-2">
            <g:if test="${i == entity.steps.size() - 1}">
                <input class="btn btn-link btn-sm" type="button" value="Remove" onclick="removeEntryRow(this, ${step.id})" />
            </g:if>
            <g:else>
                <input class="btn btn-link btn-sm" type="button" value="Remove" style="display: none;" onclick="removeEntryRow(this, ${step.id})" />
            </g:else>
        </div>
    </div>
</g:each>