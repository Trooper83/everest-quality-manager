package com.everlution

import grails.gorm.services.Service

@Service(Area)
interface AreaService {

    Area get(Serializable id)
}