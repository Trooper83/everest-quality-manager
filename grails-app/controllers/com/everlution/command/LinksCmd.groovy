package com.everlution.command

import com.everlution.StepLink
import grails.validation.Validateable

class LinksCmd implements Validateable {

    List<StepLink> links

    static boolean defaultNullable() {
        true
    }
}
