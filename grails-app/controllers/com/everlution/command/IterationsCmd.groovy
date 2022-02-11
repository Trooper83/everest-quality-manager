package com.everlution.command

import grails.validation.Validateable

class IterationsCmd implements Validateable {

    Long testCycleId
    List<Long> testGroupIds
    List<Long> testCaseIds

    static boolean defaultNullable() {
        true
    }
}
