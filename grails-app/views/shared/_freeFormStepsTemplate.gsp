<div class="card-body">
    <div class="row border-bottom">
        <p class="col-5">Action</p>
        <p class="col-5">Result</p>
    </div>
    <div id="stepsTableContent">
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
    </div>
    <input class="btn btn-secondary btn-sm mt-3" id="btnAddRow" type="button" value="Add Step" onclick="addEntryRow()" accesskey="n"/>
    <asset:image src="icons/info.svg" alt="info" width="15" height="15"
                 data-toggle="tooltip" data-placement="top" title="ALT+n to add a new row"/>
</div>
