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
        def l
        def result = Link.withCriteria {
            eq("project", link.project)
            and {
                eq("ownerId", link.ownerId)
                eq("linkedId", link.linkedId)
                eq("relation", link.relation)
            }
        }
        if (result.size() == 0) {
            l = save(link)
        }
        if (link.ownerId != link.linkedId) {
            save(createInvertedLink(link))
        }
        return l
    }

    /**
     * deletes links and their inverted links
     */
    @Transactional
    void deleteRelatedLinks(List<Long> ids) {
        for (id in ids) {
            def l = read(id)
            def r = getInvertedRelation(l.relation)
            def c = Link.createCriteria()
            def inverted = c {
                eq("project", l.project)
                and {
                    eq("ownerId", l.linkedId)
                    eq("linkedId", l.ownerId)
                    eq("relation", r)
                }
            }
            delete(id)
            delete(inverted.id)
        }
    }

    /**
     * gets the inverted relationship type
     */
    private String getInvertedRelation(String relation) {
        def r = ''
        switch (relation) {
            case Relationship.IS_CHILD_OF.name:
                r = Relationship.IS_PARENT_OF.name
                break
            case Relationship.IS_PARENT_OF.name:
                r = Relationship.IS_CHILD_OF.name
                break
            case Relationship.IS_SIBLING_OF.name:
                r = Relationship.IS_SIBLING_OF.name
                break
            default:
                r = null
        }
        return r
    }

    /**
     * creates inverted links
     */
    private Link createInvertedLink(Link link) {
        def relation = getInvertedRelation(link.relation)
        def l = new Link(ownerId: link.linkedId, linkedId: link.ownerId, project: link.project, relation: relation)
        return l
    }
}
