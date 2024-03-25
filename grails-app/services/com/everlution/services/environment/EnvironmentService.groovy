package com.everlution.services.environment

import com.everlution.domains.Environment
import grails.gorm.services.Service

@Service(Environment)
interface EnvironmentService {

    Environment get(Serializable id)
}