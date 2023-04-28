package com.everlution

import com.everlution.command.RemovedItems
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Bug)
abstract class BugService implements IBugService {

    TestStepService testStepService

    @Transactional
    SearchResult findAllByProject(Project project, Map args) {
        int c = Bug.countByProject(project)
        List bugs = Bug.findAllByProject(project, args)
        return new SearchResult(bugs, c)
    }

    /**
     * finds all bugs in the project with a name
     * that contains the string
     * @param name - the string to search
     */
    @Transactional
    SearchResult findAllInProjectByName(Project project, String s, Map args) {
        List<Bug> bugs = Bug.findAllByProjectAndNameIlike(project, "%${s}%", args)
        int c = Bug.countByProjectAndNameIlike(project, "%${s}%")
        return new SearchResult(bugs, c)
    }

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
