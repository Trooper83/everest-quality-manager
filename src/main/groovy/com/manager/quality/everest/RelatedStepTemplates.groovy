package com.manager.quality.everest

import com.manager.quality.everest.domains.StepTemplate

class RelatedStepTemplates {

    RelatedStepTemplates(StepTemplate template, List<StepTemplate> relatedStepTemplates) {
        this.template = template
        this.relatedStepTemplates = relatedStepTemplates
    }

    StepTemplate template
    List<StepTemplate> relatedStepTemplates
}
