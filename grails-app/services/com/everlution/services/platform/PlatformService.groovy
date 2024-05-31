package com.everlution.services.platform

import com.everlution.domains.Platform
import grails.gorm.services.Service

@Service(Platform)
interface PlatformService {

    Platform get(Serializable id)

}