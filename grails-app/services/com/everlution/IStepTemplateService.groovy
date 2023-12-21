package com.everlution

interface IStepTemplateService {

    StepTemplate get(Serializable id)

    StepTemplate read(Serializable id)

    StepTemplate save(StepTemplate stepTemplate)
}