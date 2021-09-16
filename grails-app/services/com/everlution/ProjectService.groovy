package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Project)
abstract class ProjectService implements IProjectService {

    TestCaseService testCaseService

    @Transactional
    @Override
    void delete(Serializable id) {
        def project = get(id)
        testCaseService.deleteAllTestCasesByProject(project)
        project.delete()
    }
}
