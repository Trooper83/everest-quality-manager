package com.everlution

import com.everlution.command.RemovedItems
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Bug)
abstract class BugService implements IBugService {

    StepService stepService

    @Transactional
    void delete(Serializable id) {
        def b = get(id)
        if (b) {
            List steps = b.steps
            b.delete()
            for (Step step in steps) {
                if (!step.isBuilderStep) {
                    stepService.delete(step.id)
                }
            }
        }
    }

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
        def steps = []
        for(id in removedItems.stepIds) {
            def step = stepService.get(id)
            if (step) {
                steps.add(step)
                bug.removeFromSteps(step)
            }
        }
        def updated = save(bug)
        for (Step step in steps) {
            if (!step.isBuilderStep) {
                stepService.delete(step.id)
            }
        }
        return updated
    }
}
