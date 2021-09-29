package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Bug)
abstract class BugService implements IBugService {

    @Transactional
    void deleteAllBugsByProject(Project project) {
        def bugs = Bug.findAllByProject(project)
        bugs.each {
            it.delete()
        }
    }
}
