package com.manager.quality.everest.services.bug

import com.manager.quality.everest.SearchResult
import com.manager.quality.everest.services.step.StepService
import com.manager.quality.everest.controllers.command.RemovedItems
import com.manager.quality.everest.domains.Bug
import com.manager.quality.everest.domains.Project
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Bug)
abstract class BugService implements IBugService {

    StepService stepService

    /**
     * find all bugs in project
     */
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
     * save an updated bug, deletes any removed free-form steps
     * @param bug - the bug to update
     * @param removedItems - ids of the steps to remove
     * @return - the updated bug
     */
    @Transactional
    Bug saveUpdate(Bug bug, RemovedItems removedItems) {
        for(id in removedItems.stepIds) {
            def step = stepService.get(id)
            if (step) {
                bug.removeFromSteps(step)
            }
        }
        return save(bug)
    }
}
