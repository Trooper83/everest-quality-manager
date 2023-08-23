package com.everlution

import grails.gorm.services.Service

@Service(StepLink)
abstract class StepLinkService implements IStepLinkService {

    /**
     * gets all related steps
     * @param step - the step to find relations for
     * @return a map containing all related steps
     */
    Map<String, List<StepLink>> getRelatedSteps(Step step) {
        List<StepLink> children = StepLink.findAllByProjectAndRelationAndOwner(step.project, 'PARENT_CHILD', step)
        List<StepLink> parents = StepLink.findAllByProjectAndRelationAndOwner(step.project, 'CHILD_PARENT', step)
        List<StepLink> siblings = StepLink.findAllByProjectAndRelationAndOwner(step.project, 'SIBLING', step)
        return ['children':children, 'parents': parents, 'siblings':siblings]
    }
}
