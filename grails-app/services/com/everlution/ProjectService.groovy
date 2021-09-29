package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Project)
abstract class ProjectService implements IProjectService {

    BugService bugService
    TestCaseService testCaseService

    @Transactional
    @Override
    void delete(Serializable id) {
        def project = get(id)
        testCaseService.deleteAllTestCasesByProject(project)
        bugService.deleteAllBugsByProject(project)
        project.delete()
    }
}
