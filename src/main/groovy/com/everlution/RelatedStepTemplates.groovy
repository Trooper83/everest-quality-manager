package com.everlution

import com.everlution.domains.StepTemplate

class RelatedStepTemplates {

    RelatedStepTemplates(StepTemplate template, List<StepTemplate> relatedStepTemplates) {
        this.template = template
        this.relatedStepTemplates = relatedStepTemplates
    }

    StepTemplate template
    List<StepTemplate> relatedStepTemplates
}
