package com.everlution

import com.everlution.command.RemovedItems
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Bug)
abstract class BugService implements IBugService {

    TestStepService testStepService

    @Transactional
    void deleteAllBugsByProject(Project project) {
        def bugs = Bug.findAllByProject(project)
        bugs.each {
            it.delete()
        }
    }

    /**
     * save an updated bug, deletes any removed steps
     * @param bug - the bug to update
     * @param removedItems - ids of the steps to remove
     * @return - the updated bug
     */
    @Transactional
    Bug saveUpdate(Bug bug, RemovedItems removedItems) {
        for(id in removedItems.ids) {
            def step = testStepService.get(id)
            bug.removeFromSteps(step)
        }
        return bug.save()
    }
}
