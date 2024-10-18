package com.manager.quality.everest.services.platform

import com.manager.quality.everest.domains.Platform
import grails.gorm.services.Service

@Service(Platform)
interface PlatformService {

    Platform get(Serializable id)

}