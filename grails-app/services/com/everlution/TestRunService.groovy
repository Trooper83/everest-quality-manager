package com.everlution

import grails.gorm.services.Service

@Service(TestRun)
interface TestRunService {

    TestRun save(TestRun testRun)
}