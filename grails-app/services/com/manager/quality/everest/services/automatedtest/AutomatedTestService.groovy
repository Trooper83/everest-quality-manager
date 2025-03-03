package com.manager.quality.everest.services.automatedtest

import com.manager.quality.everest.SearchResult
import com.manager.quality.everest.domains.AutomatedTest
import com.manager.quality.everest.domains.Project
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import grails.validation.ValidationException

@Service(AutomatedTest)
abstract class AutomatedTestService implements IAutomatedTestService {

    @Transactional
    SearchResult findAllByProject(Project project, Map args) {
        int c = AutomatedTest.countByProject(project)
        List a = AutomatedTest.findAllByProject(project, args)
        return new SearchResult(a, c)
    }

    @Transactional
    SearchResult findAllInProjectByFullName(Project project, String fullName, Map args) {
        int c = AutomatedTest.countByProjectAndFullNameIlike(project, "%${fullName}%")
        List a = AutomatedTest.findAllByProjectAndFullNameIlike(project, "%${fullName}%", args)
        return new SearchResult(a, c)
    }

    @Transactional
    AutomatedTest findOrSave(Project project, String fullName) throws ValidationException {
        def a = findByProjectAndFullName(project, fullName)
        return a != null ? a : save(new AutomatedTest(project: project, fullName: fullName))
    }
}
