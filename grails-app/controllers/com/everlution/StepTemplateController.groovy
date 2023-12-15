package com.everlution

import com.everlution.command.LinksCmd
import com.everlution.command.RemovedItems
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

class StepTemplateController {

    LinkService linkService
    ProjectService projectService
    SpringSecurityService springSecurityService
    StepTemplateService stepTemplateService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", search: "GET", getRelatedTemplates: "GET"]

    /**
     * lists all templates or perform search
     * if [params.isSearch = true]
     * /stepTemplates
     * @return - list of stepTemplates
     */
    @Secured("ROLE_READ_ONLY")
    def stepTemplates(Long projectId, Integer max) {

        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }

        params.max = Math.min(max ?: 25, 100)
        def searchResult
        if(!params.isSearch) { // load view
            searchResult = stepTemplateService.findAllInProject(project, params)

        } else { // perform search
            searchResult = stepTemplateService.findAllInProjectByName(project, params.name, params)
        }
        respond searchResult.results, model: [stepTemplateCount: searchResult.count, project: project], view: 'stepTemplates'
    }

    /**
     * displays the create view
     * /stepTemplate/create
     */
    @Secured("ROLE_BASIC")
    def create(Long projectId) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }
        respond new StepTemplate(params), model: [project: project], view: 'create'
    }

    /**
     * saves a new instance
     * @param template - the template to save
     */
    @Secured("ROLE_BASIC")
    def save(StepTemplate template, LinksCmd links) {
        withForm {
            if (template == null) {
                notFound()
                return
            }

            template.person = springSecurityService.getCurrentUser() as Person

            try {
                stepTemplateService.save(template)
            } catch (ValidationException ignored) {
                def project = projectService.read(template.project.id)
                respond template.errors, view:'create', model: [ project: project ]
                return
            }
            try {
                links.links?.removeAll( l -> l == null)
                links.links?.each { link ->
                    link.project = template.project
                    link.ownerId = template.id
                    linkService.createSave(link)
                }
            } catch (ValidationException ignored) {
                flash.error = "An error occurred attempting to link templates"
            }
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'stepTemplate.label', default: 'StepTemplate'), template.id])
                    redirect uri: "/project/${template.project.id}/stepTemplate/show/${template.id}"
                }
                '*' { respond template, [status: CREATED] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * displays the show view
     * /stepTemplate/show/${id}
     * @param id - id of the item
     * @return - the item to show
     */
    @Secured("ROLE_READ_ONLY")
    def show(Long id) {
        def template = stepTemplateService.get(id)
        def relations = stepTemplateService.getLinkedTemplatesByRelation(template)
        respond template, model: [relations: relations], view: 'show'
    }

    /**
     * displays the edit view
     * /stepTemplate/edit/${id}
     * @param id - id of the item
     */
    @Secured("ROLE_BASIC")
    def edit(Long id) {
        def template = stepTemplateService.get(id)
        def relatedTemplates = stepTemplateService.getLinkedTemplatesByRelation(template)
        respond template, view: "edit", model: [linkedMap: relatedTemplates]
    }

    /**
     * updates an items data
     * @param template - item to update
     */
    @Secured("ROLE_BASIC")
    def update(StepTemplate template, Long projectId, LinksCmd links, RemovedItems removedItems) {
        withForm {
            if (template == null || projectId == null) {
                notFound()
                return
            }
            def templateProjectId = template.project.id
            if (projectId != templateProjectId) {
                notFound()
                return
            }

            try {
                stepTemplateService.save(template)
            } catch (ValidationException e) {
                def s = stepTemplateService.read(template.id)
                def l = stepTemplateService.getLinkedTemplatesByRelation(s)
                s.errors = e.errors
                render view: 'edit', model: [stepTemplate: s, linkedMap: l]
                return
            }
            try {
                links.links?.removeAll( l -> l == null)
                links.links?.each { link ->
                    link.project = template.project
                    link.ownerId = template.id
                    linkService.createSave(link)
                }
            } catch (ValidationException ignored) {
                flash.error = "An error occurred attempting to link templates"
            }
            try {
                linkService.deleteRelatedLinks(removedItems.linkIds)
            } catch (Exception ignored) {
                flash.error = "An error occurred attempting to delete links"
            }
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.updated.message',
                            args: [message(code: 'stepTemplate.label', default: 'StepTemplate'), template.id])
                    redirect uri: "/project/${template.project.id}/stepTemplate/show/${template.id}"
                }
                '*'{ respond template, [status: OK] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * deletes an instance
     * @param id - id of the instance to delete
     */
    @Secured("ROLE_BASIC")
    def delete(Long id, Long projectId) {
        withForm {
            if (id == null || projectId == null) {
                notFound()
                return
            }

            def template = stepTemplateService.read(id)
            if (template.project.id != projectId) {
                notFound()
                return
            }

            try {
                stepTemplateService.delete(id)
            } catch (Exception ignored) {
                flash.error = "An issue occurred attempting to delete the template"
                redirect uri: "/project/${template.project.id}/stepTemplate/show/${template.id}"
                return
            }
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'stepTemplate.label', default: 'StepTemplate'), id])
                    redirect uri: "/project/${projectId}/stepTemplates"
                }
                '*' { render status: NO_CONTENT }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * searches for templates by name
     */
    @Secured("ROLE_BASIC")
    def search(Long projectId, String q) {

        def project = projectService.read(projectId)
        if (project == null) {
            notFound()
            return
        }
        params.max = 7
        def searchResult = stepTemplateService.findAllInProjectByName(project, q, params)
        respond searchResult.results, formats: ['json']
    }

    /**
     * gets related templates for the supplied template
     */
    @Secured("ROLE_BASIC")
    def getRelatedTemplates(Long projectId, Long templateId) {

        def project = projectService.read(projectId)
        if (templateId == null || project == null) {
            notFound()
            return
        }
        def templates = stepTemplateService.getRelatedTemplates(templateId)

        if (!templates.template) {
            notFound()
            return
        }
        respond templates, formats: ['json']
    }

    /**
     * displays view when notFound (404)
     */
    protected void notFound() {
        request.withFormat {
            '*'{ render status: NOT_FOUND }
        }
    }

    /**
     * displays error view (500)
     */
    protected void error() {
        request.withFormat {
            '*'{ render status: INTERNAL_SERVER_ERROR }
        }
    }
}
