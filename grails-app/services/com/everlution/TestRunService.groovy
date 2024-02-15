package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import grails.validation.ValidationException

@Service(TestRun)
abstract class TestRunService implements ITestRunService {

    @Transactional
    TestRun save(TestRun testRun) {
        if(!testRun.validate()) {
            throw new ValidationException("TestRun failed to validate", testRun.errors)
        }
        testRun.save(flush: true)
    }
}
