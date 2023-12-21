<ul class="nav nav-tabs" id="myTab" role="tablist">
    <li class="nav-item" role="presentation">
        <button class="nav-link active" id="builder-tab" data-bs-toggle="tab"
                data-bs-target="#builder-tab-pane" type="button" role="tab"
                aria-controls="builder-tab-pane" aria-selected="true"
                onclick="resetForm('free');">Builder</button>
    </li>
    <li class="nav-item" role="presentation">
        <button class="nav-link" id="free-form-tab" data-bs-toggle="tab"
                data-bs-target="#free-form-tab-pane" type="button" role="tab"
                aria-controls="free-form-tab-pane" aria-selected="false"
                onclick="resetForm('builder');">Free Form</button>
    </li>
</ul>
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
                <div class="col-9" id="builderSteps" style="min-height:2em;"></div>
                <div class="col-3" id="suggestedSteps"></div>
            </div>
        </div>
    </div>
    <div class="tab-pane fade" id="free-form-tab-pane" role="tabpanel" aria-labelledby="free-form-tab" tabindex="0">
        <div class="card-body">
            <div class="row border-bottom">
                <p class="col">Action</p>
                <p class="col">Data</p>
                <p class="col">Result</p>
                <p class="col-1"></p>
            </div>
            <div id="stepsTableContent"></div>
            <input class="btn btn-secondary btn-sm mt-3" id="btnAddRow" type="button" value="Add Step" onclick="addEntryRow()" accesskey="n"/>
            <asset:image src="icons/info.svg" alt="info" width="15" height="15"
                         data-toggle="tooltip" data-placement="top" title="ALT+n to add a new row"/>
        </div>
    </div>
</div>