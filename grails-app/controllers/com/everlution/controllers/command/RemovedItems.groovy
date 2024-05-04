package com.everlution.controllers.command

import grails.validation.Validateable

/**
 * command object to track removed items for edit workflow
 */
class RemovedItems implements Validateable {

    List<String> areaIds
    List<String> environmentIds
    List<Long> linkIds
    List<String> platformIds
    List<String> stepIds

    static boolean defaultNullable() {
        true
    }
}
