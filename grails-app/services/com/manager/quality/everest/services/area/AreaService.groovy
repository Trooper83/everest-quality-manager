package com.manager.quality.everest.services.area

import com.manager.quality.everest.domains.Area
import grails.gorm.services.Service

@Service(Area)
interface AreaService {

    Area get(Serializable id)
}