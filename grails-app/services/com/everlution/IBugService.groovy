package com.everlution

import grails.gorm.services.Service

interface IBugService {

    Bug get(Serializable id)

    List<Bug> list(Map args)

    Long count()

    void delete(Serializable id)

    Bug save(Bug bug)

}