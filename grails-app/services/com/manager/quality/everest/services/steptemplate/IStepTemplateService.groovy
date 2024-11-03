package com.manager.quality.everest.services.steptemplate

import com.manager.quality.everest.domains.StepTemplate

interface IStepTemplateService {

    StepTemplate get(Serializable id)

    StepTemplate read(Serializable id)

    StepTemplate save(StepTemplate stepTemplate)
}