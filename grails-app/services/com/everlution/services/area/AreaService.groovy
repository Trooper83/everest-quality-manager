package com.everlution.services.area

import com.everlution.domains.Area
import grails.gorm.services.Service

@Service(Area)
interface AreaService {

    Area get(Serializable id)
}