package com.everlution

import grails.gorm.services.Service

@Service(Project)
abstract class ProjectService implements IProjectService {

    TestCaseService testCaseService

    @Override
    void delete(Serializable id) {
        def project = get(id)
        testCaseService.deleteAllTestCasesByProject(project)
        project.delete()
    }
}
