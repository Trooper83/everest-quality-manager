package com.manager.quality.everest.services.step

import com.manager.quality.everest.domains.Step
import com.manager.quality.everest.domains.StepTemplate
import grails.gorm.services.Service

@Service(Step)
interface StepService {

    List<Step> findAllByTemplate(StepTemplate template)

    Step get(Serializable id)

    Step read(Serializable id)

    Step save(Step step)
}