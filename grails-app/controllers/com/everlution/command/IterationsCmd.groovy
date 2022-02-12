package com.everlution.command

import grails.validation.Validateable

class IterationsCmd implements Validateable {

    Long testCycleId
    List<Long> testGroups
    List<Long> testCases

    static boolean defaultNullable() {
        true
    }
}
