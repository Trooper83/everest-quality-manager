package com.everlution

import grails.gorm.services.Service

@Service(Link)
abstract class LinkService implements ILinkService {

    /**
     * gets all related links
     */
    List<Link> getLinks(Long id, Project project) {
        if (!id || !project) {
            return []
        }
        def c = Link.createCriteria()
        def links = c {
            eq("project", project)
            or {
                eq("ownerId", id)
                eq("linkedId", id)
            }
        }
        return links
    }
}
