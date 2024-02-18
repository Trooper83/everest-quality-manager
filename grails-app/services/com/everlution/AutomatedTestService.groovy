package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import grails.validation.ValidationException

@Service(AutomatedTest)
abstract class AutomatedTestService implements IAutomatedTestService {

    List<AutomatedTest> findAllInProjectByFullName(Project project, String fullName, Map args) {
        AutomatedTest.findAllByProjectAndFullNameIlike(project, "%${fullName}%", args)
    }

    @Transactional
    AutomatedTest findOrSave(Project project, String fullName) throws ValidationException {
        def a = findByProjectAndFullName(project, fullName)
        return a != null ? a : save(new AutomatedTest(project: project, fullName: fullName))
    }
}
