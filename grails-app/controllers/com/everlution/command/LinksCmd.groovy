package com.everlution.command

import com.everlution.Link
import grails.validation.Validateable

class LinksCmd implements Validateable {

    List<Link> links

    static boolean defaultNullable() {
        true
    }
}
