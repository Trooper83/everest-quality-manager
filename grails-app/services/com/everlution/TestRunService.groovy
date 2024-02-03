package com.everlution

import grails.gorm.services.Service

@Service(TestRun)
interface TestRunService {

    TestRun get(Serializable id)

    TestRun save(TestRun testRun)
}