package com.everlution.services.releaseplan

import com.everlution.SearchResult
import com.everlution.domains.Project
import com.everlution.domains.ReleasePlan
import com.everlution.domains.TestCycle
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
     * gets the next and previous release plans
     */
    LinkedHashMap<String, List<ReleasePlan>> getPlansByStatus(Project project) {

        def releasePlans = findAllByProject(project, [:]).results

        List<ReleasePlan> previous = List.copyOf(releasePlans)
        def released = previous
                .findAll {it.releaseDate != null }
                .findAll {it.status == 'Released' }
                .sort { it.releaseDate }
                .reverse(true)
                .take(3)

        List<ReleasePlan> inProgressCopy = List.copyOf(releasePlans)
        def inProgress = inProgressCopy
                .findAll {it.plannedDate != null }
                .findAll {it.releaseDate == null }
                .findAll {it.status == 'In Progress' }
                .sort { it.plannedDate }
                .take(3)


        List<ReleasePlan> nextCopy = List.copyOf(releasePlans)
        def next = nextCopy
                .findAll {it.status == 'Planning' }
                .sort { it.dateCreated }
                .take(3)
        return [ next: next, released: released, inProgress: inProgress ]
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
