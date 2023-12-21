package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(StepTemplate)
abstract class StepTemplateService implements IStepTemplateService {

    LinkService linkService
    StepService stepService

    @Transactional
    void delete(Serializable id) {
        def template = get(id)
        if (template) {
            def links = linkService.getLinks(template.id, template.project)
            def steps = stepService.findAllByTemplate(template)
            template.delete()
            links.each {
                linkService.delete(it.id)
            }
            steps.each {
                it.template = null
                stepService.save(it)
            }
        }
    }

    /**
     * finds all templates in a project
     */
    @Transactional
    SearchResult findAllInProject(Project project, Map args) {
        int c = StepTemplate.countByProject(project)
        List templates = StepTemplate.findAllByProject(project, args)
        return new SearchResult(templates, c)
    }

    /**
     * finds all templates in the project with a name
     * that contains the string
     * @param name - the string to search
     */
    @Transactional
    SearchResult findAllInProjectByName(Project project, String s, Map args) {
        List templates = StepTemplate.findAllByProjectAndNameIlike(project, "%${s}%", args)
        int c = StepTemplate.countByProjectAndNameIlike(project, "%${s}%")
        return new SearchResult(templates, c)
    }

    /**
     * gets all related templates
     */
    Map<String, List<LinkItem>> getLinkedTemplatesByRelation(StepTemplate template) {
        if (!template) {
            return [children: [], parents: [], siblings: []]
        }

        List<Link> links = linkService.getLinks(template.id, template.project)
        links.removeAll { it -> it.ownerId != template.id }

        List<LinkItem> children = [], parents = [], siblings = []

        def linkMap = links.groupBy { it -> it.relation }

        linkMap[Relationship.IS_PARENT_OF.name].each { it -> children.add(new LinkItem(it.id, get(it.linkedId)))}

        linkMap[Relationship.IS_CHILD_OF.name].each { it -> parents.add(new LinkItem(it.id, get(it.linkedId)))}

        linkMap[Relationship.IS_SIBLING_OF.name].each { it -> siblings.add(new LinkItem(it.id, get(it.linkedId)))}

        return [children: children, parents: parents, siblings: siblings]
    }

    /**
     * gets related templates in which the supplied template id is the owner id
     */
    RelatedStepTemplates getRelatedTemplates(Long id) {
        def template = read(id)
        if (!template) {
            return new RelatedStepTemplates(null, [])
        }

        List<Link> links = linkService.getLinks(id, template.project)
        links.removeAll { it -> it.ownerId != id }
        def related = []
        links.each { it -> related.add(read(it.linkedId)) }
        return new RelatedStepTemplates(template, related)
    }

    @Transactional
    StepTemplate update(StepTemplate template) {
        def actDirty = template.isDirty('act')
        def resDirty = template.isDirty('result')
        def t = save(template)
        if (actDirty || resDirty) {
            def steps = stepService.findAllByTemplate(template)
            steps.every {
                it.act = template.act
                it.result = template.result
                stepService.save(it)
            }
        }
        return t
    }
}
