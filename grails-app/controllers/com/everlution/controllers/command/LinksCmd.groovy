package com.everlution.controllers.command

import com.everlution.domains.Link
import grails.validation.Validateable

class LinksCmd implements Validateable {

    List<Link> links

    static boolean defaultNullable() {
        true
    }
}
