<div class="card-body">
    <div class="row border-bottom">
        <p class="col">Action</p>
        <p class="col">Data</p>
        <p class="col">Result</p>
        <p class="col-1"></p>
    </div>
    <div id="stepsTableContent">
        <g:each status="i" var="step" in="${entity.steps}">
            <div class="row align-items-center mt-3">
                <g:hiddenField name="stepsIndex[${i}]" class="iHidden" />
                <div class="col">
                    <g:textArea class="form-control" type="text" name="steps[${i}].act" value="${step.act}" />
                </div>
                <div class="col">
                    <g:textArea class="form-control" type="text" name="steps[${i}].data" value="${step.data}" />
                </div>
                <div class="col">
                    <g:textArea class="form-control" type="text" name="steps[${i}].result" value="${step.result}" />
                </div>
                <div class="col-md-1">
                    <g:if test="${i == entity.steps.size() - 1}">
                        <input class="btn btn-link btn-sm" type="button" value="Remove" onclick="removeEntryRow(this, ${step.id})" />
                    </g:if>
                    <g:else>
                        <input class="btn btn-link btn-sm" type="button" value="Remove" style="display: none;" onclick="removeEntryRow(this, ${step.id})" />
                    </g:else>
                </div>
            </div>
        </g:each>
    </div>
    <input class="btn btn-secondary btn-sm mt-3" id="btnAddRow" type="button" value="Add Step" onclick="addEntryRow()" accesskey="n"/>
</div>
