package com.everlution

import grails.gorm.services.Service

@Service(TestGroup)
interface TestGroupService {

    TestGroup get(Serializable id)

    List<TestGroup> list(Map args)

    Long count()

    void delete(Serializable id)

    TestGroup save(TestGroup testGroup)

}