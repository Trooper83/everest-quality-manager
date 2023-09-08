package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

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

    /**
     * creates bi-directional links and saves
     */
    @Transactional
    Link createSave(Link link) {
        def inverted = createInvertedLink(link)
        save(link)
        save(inverted)
    }

    /**
     * creates inverted links
     */
    private Link createInvertedLink(Link link) {
        def l = new Link(ownerId: link.linkedId, linkedId: link.ownerId, project: link.project)
        switch (link.relation) {
            case Relationship.IS_CHILD_OF.name:
                l.relation = Relationship.IS_PARENT_OF.name
                break
            case Relationship.IS_PARENT_OF.name:
                l.relation = Relationship.IS_CHILD_OF.name
                break
            default:
                l.relation = Relationship.IS_SIBLING_OF.name
        }
        return l
    }
}
