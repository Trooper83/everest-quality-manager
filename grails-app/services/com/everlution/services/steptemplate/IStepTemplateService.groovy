package com.everlution.services.steptemplate

import com.everlution.domains.StepTemplate

interface IStepTemplateService {

    StepTemplate get(Serializable id)

    StepTemplate read(Serializable id)

    StepTemplate save(StepTemplate stepTemplate)
}