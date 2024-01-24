package com.everlution

import grails.gorm.services.Service

@Service(TestResult)
interface TestResultService {

    TestResult save(TestResult testResult)
}