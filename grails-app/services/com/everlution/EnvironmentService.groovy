package com.everlution

import grails.gorm.services.Service

@Service(Environment)
interface EnvironmentService {

    Environment get(Serializable id)
}