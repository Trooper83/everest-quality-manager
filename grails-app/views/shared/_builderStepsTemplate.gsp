<div class="tab-content" id="myTabContent">
    <div class="tab-pane fade show active mb-3" id="builder-tab-pane" role="tabpanel"
         aria-labelledby="builder-tab" tabindex="0">
        <div class="card-body">
            <div class="row border-bottom">
                <p class="col-8">Step Search</p>
                <p class="col-4">Current Step</p>
            </div>
            <div class="row mt-3">
                <div class="col-6">
                    <g:textField class="form-control" name="search" type="text" placeholder="Step Name"
                                 autocomplete="off"/>
                    <ul class="search-results-menu col-6" id="search-results" style="position:absolute; z-index:999;"></ul>
                </div>
                <div class="col-4 offset-2" id="currentStep"></div>
            </div>
            <div class="row border-bottom mb-3 mt-5">
                <p class="col">Action</p>
                <p class="col">Data</p>
                <p class="col">Result</p>
                <p class="col">Suggested</p>
            </div>
            <div class="row mt-3">
                <div class="col-9" id="builderSteps" style="min-height:2em;">
                    <g:each status="i" var="step" in="${entity.steps}">
                        <div class="row align-items-center mb-2">
                            <g:hiddenField name="stepsIndex[${i}]" class="iHidden" />
                            <div class="col">
                                <g:textArea class="form-control" name="steps[${i}].act" readonly="true"
                                            style="min-height:3.5em; max-height:7em;">${step.act}</g:textArea>
                            </div>
                            <div class="col">
                                <g:textArea class="form-control" name="steps[${i}].data"
                                        style="min-height:3.5em; max-height:7em;">${step.data}</g:textArea>
                            </div>
                            <div class="col">
                                <g:textArea class="form-control" name="steps[${i}].result" readonly="true"
                                            style="min-height:3.5em; max-height:7em;">${step.result}</g:textArea>
                            </div>
                            <div>
                                <g:if test="${i == entity.steps.size() - 1}">
                                    <input class="btn btn-link btn-sm" type="button" value="Remove"
                                           onclick="removeBuilderRow(this, ${step.id});" />
                                </g:if>
                                <g:else>
                                    <input class="btn btn-link btn-sm" type="button" value="Remove" style="display: none;"
                                           onclick="removeBuilderRow(this, ${step.id});" />
                                </g:else>
                            </div>
                            <g:hiddenField name="steps[${i}].id" data-name="hiddenId" value="${step.template.id}"/>
                        </div>
                    </g:each>
                </div>
                <div class="col-3" id="suggestedSteps"></div>
            </div>
        </div>
    </div>
</div>