package com.everlution

import grails.gorm.transactions.Transactional
import grails.validation.ValidationException

class AutomatedTestService {

    @Transactional
    AutomatedTest findOrSave(Project project, String fullName) {
        def a = AutomatedTest.findOrSaveByProjectAndFullName(project, fullName)
        if(a.hasErrors()) {
            throw new ValidationException("Failed to validate", a.errors)
        }
        return a
    }
}
