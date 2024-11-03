package com.manager.quality.everest.services.environment

import com.manager.quality.everest.domains.Environment
import grails.gorm.services.Service

@Service(Environment)
interface EnvironmentService {

    Environment get(Serializable id)
}