<div class="modal fade" id="stepsModal" tabindex="-1" aria-labelledby="stepsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="testCycleModalLabel">Add Test Steps</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"
                        data-test-id="modal-close-button">
                </button>
            </div>
            <div class="modal-body">
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
                    <div class="row border-bottom mt-5">
                        <p class="col">Action</p>
                        <p class="col">Data</p>
                        <p class="col">Result</p>
                        <p class="col">Suggested</p>
                    </div>
                    <div class="row">
                        <div class="col-9" id="builderSteps" style="min-height:2em;"></div>
                        <div class="col-3" id="suggestedSteps"></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <input class="btn btn-primary btn-sm" id="btnAppendSteps" type="button" value="Add Steps"
                       data-bs-dismiss="modal" onclick="appendBuilderSteps()"/>
            </div>
        </div>
    </div>
</div>
<asset:javascript src="jquery-3.3.1.min.js"/>