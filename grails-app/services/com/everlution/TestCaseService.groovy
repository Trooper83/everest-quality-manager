package com.everlution

import grails.gorm.services.Service

@Service(TestCase)
interface TestCaseService {

    TestCase get(Serializable id)

    List<TestCase> list(Map args)

    Long count()

    void delete(Serializable id)

    TestCase save(TestCase testCase)

}