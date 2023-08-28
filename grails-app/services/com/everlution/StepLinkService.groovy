package com.everlution

import grails.gorm.services.Service

@Service(StepLink)
abstract class StepLinkService implements IStepLinkService {

    /**
     * gets all related steps
     * @param step - the step to find relations for
     * @return a map containing all related steps
     */
    List<StepLink> getStepLinks(Step step) {
        if (!step) {
            return []
        }
        def c = StepLink.createCriteria()
        def links = c {
            eq("project", step.project)
            or {
                eq("owner", step)
                eq("linkedStep", step)
            }
        }
        return links
    }

    /**
     * gets all related steps
     * @param step - the step to find relations for
     * @return a map containing all related steps by type
     */
    Map<String, List<StepLink>> getStepLinksByType(Step step) {
        if (!step) {
            return [:]
        }
        List<StepLink> children = StepLink.findAllByProjectAndRelationAndOwner(step.project, Relationship.IS_PARENT_OF.name, step)
        List<StepLink> parents = StepLink.findAllByProjectAndRelationAndOwner(step.project, Relationship.IS_CHILD_OF.name, step)
        List<StepLink> siblings = StepLink.findAllByProjectAndRelationAndOwner(step.project, Relationship.IS_SIBLING_OF.name, step)
        return ['children':children, 'parents': parents, 'siblings':siblings]
    }
}
