package com.manager.quality.everest.controllers.command

import com.manager.quality.everest.domains.Link
import grails.validation.Validateable

class LinksCmd implements Validateable {

    List<Link> links

    static boolean defaultNullable() {
        true
    }
}
