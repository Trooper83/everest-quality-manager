package com.everlution

import com.everlution.command.RemovedItems
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Bug)
abstract class BugService implements IBugService {

    TestStepService testStepService

    /**
     * save an updated bug, deletes any removed steps
     * @param bug - the bug to update
     * @param removedItems - ids of the steps to remove
     * @return - the updated bug
     */
    @Transactional
    Bug saveUpdate(Bug bug, RemovedItems removedItems) {
        for(id in removedItems.stepIds) {
            def step = testStepService.get(id)
            bug.removeFromSteps(step)
        }
        return save(bug)
    }
}
