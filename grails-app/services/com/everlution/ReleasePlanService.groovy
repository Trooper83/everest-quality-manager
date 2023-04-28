package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(ReleasePlan)
abstract class ReleasePlanService implements IReleasePlanService {

    /**
     * adds a test cycle to the release plan
     */
    @Transactional
    ReleasePlan addTestCycle(ReleasePlan releasePlan, TestCycle testCycle) {
      return save(releasePlan.addToTestCycles(testCycle))
    }

    /**
     * finds all plans in the project with a name
     * that contains the string
     * @param name - the string to search
     */
    @Transactional
    SearchResult findAllInProjectByName(Project project, String s, Map args) {
        List<ReleasePlan> plans = ReleasePlan.findAllByProjectAndNameIlike(project, "%${s}%", args)
        int c = ReleasePlan.countByProjectAndNameIlike(project, "%${s}%")
        return new SearchResult(plans, c)
    }

    /**
     * gets all plans in the domain with the associated project
     * @param projectId - id of the project
     * @return - list of all plans with the project
     */
    @Transactional
    SearchResult findAllByProject(Project project, Map args) {
        List<ReleasePlan> plans = ReleasePlan.findAllByProject(project, args)
        int c = ReleasePlan.countByProject(project)
        return new SearchResult(plans, c)
    }

    /**
     * removes a test cycle from a release plan
     * @param releasePlan
     * @param testCycle
     */
    @Transactional
    void removeTestCycle(ReleasePlan releasePlan, TestCycle testCycle) {
        releasePlan.removeFromTestCycles(testCycle)
    }
}
