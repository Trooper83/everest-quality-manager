package com.everlution.command

import grails.validation.Validateable

/**
 * command object to track removed items for edit workflow
 */
class RemovedItems implements Validateable {

    List<String> ids

    static boolean defaultNullable() {
        true
    }
}