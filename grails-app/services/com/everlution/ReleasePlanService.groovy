package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(ReleasePlan)
abstract class ReleasePlanService implements IReleasePlanService {

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
