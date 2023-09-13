package com.everlution

import com.everlution.command.LinksCmd
import com.everlution.command.RemovedItems
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class StepController {

    LinkService linkService
    ProjectService projectService
    SpringSecurityService springSecurityService
    StepService stepService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", search: "GET", getRelatedSteps: "GET"]

    /**
     * lists all steps or perform search
     * if [params.isSearch = true]
     * /steps
     * @return - list of steps
     */
    @Secured("ROLE_READ_ONLY")
    def steps(Long projectId, Integer max) {

        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }

        params.max = Math.min(max ?: 10, 100)
        def searchResult
        if(!params.isSearch) { // load view
            searchResult = stepService.findAllByProject(project, params)

        } else { // perform search
            searchResult = stepService.findAllInProjectByName(project, params.name, params)
        }
        respond searchResult.results, model: [stepCount: searchResult.count, project: project], view: 'steps'
    }

    /**
     * displays the show view
     * /step/show/${id}
     * @param id - id of the item
     * @return - the item to show
     */
    @Secured("ROLE_READ_ONLY")
    def show(Long id) {
        def step = stepService.get(id)
        def relations = stepService.getLinkedStepsByRelation(step)
        respond step, model: [relations: relations], view: 'show'
    }

    /**
     * displays the create view
     * /step/create
     */
    @Secured("ROLE_BASIC")
    def create(Long projectId) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }
        respond new Step(params), model: [project: project], view: 'create'
    }

    /**
     * saves a new instance
     * @param step - the step to save
     */
    @Secured("ROLE_BASIC")
    def save(Step step, LinksCmd links) {
        withForm {
            if (step == null) {
                notFound()
                return
            }

            step.person = springSecurityService.getCurrentUser() as Person

            try {
                stepService.save(step)
            } catch (ValidationException ignored) {
                def project = projectService.read(step.project.id)
                respond step.errors, view:'create', model: [ project: project ]
                return
            }
            try {
                links.links?.removeAll( l -> l == null)
                links.links?.each { link ->
                    link.project = step.project
                    link.ownerId = step.id
                    linkService.createSave(link)
                }
            } catch (ValidationException ignored) {
                flash.error = "An error occurred attempting to link steps"
            }
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'step.label', default: 'Step'), step.id])
                    redirect uri: "/project/${step.project.id}/step/show/${step.id}"
                }
                '*' { respond step, [status: CREATED] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * displays the edit view
     * /step/edit/${id}
     * @param id - id of the item
     */
    @Secured("ROLE_BASIC")
    def edit(Long id) {
        def step = stepService.get(id)
        def relatedSteps = stepService.getLinkedStepsByRelation(step)
        respond step, view: "edit", model: [linkedMap: relatedSteps]
    }

    /**
     * updates an items data
     * @param step - item to update
     */
    @Secured("ROLE_BASIC")
    def update(Step step, Long projectId, LinksCmd links, RemovedItems removedItems) {
        withForm {
            if (step == null || projectId == null) {
                notFound()
                return
            }
            def stepProjectId = step.project.id
            if (projectId != stepProjectId) {
                notFound()
                return
            }

            try {
                stepService.save(step)
            } catch (ValidationException e) {
                def s = stepService.read(step.id)
                def l = stepService.getLinkedStepsByRelation(s)
                s.errors = e.errors
                render view: 'edit', model: [step: s, linkedMap: l]
                return
            }
            try {
                links.links?.removeAll( l -> l == null)
                links.links?.each { link ->
                    link.project = step.project
                    link.ownerId = step.id
                    linkService.createSave(link)
                }
            } catch (ValidationException ignored) {
                flash.error = "An error occurred attempting to link steps"
            }
            try {
                linkService.deleteRelatedLinks(removedItems.linkIds)
            } catch (Exception ignored) {
                flash.error = "An error occurred attempting to delete links"
            }
            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.updated.message',
                            args: [message(code: 'step.label', default: 'Step'), step.id])
                    redirect uri: "/project/${step.project.id}/step/show/${step.id}"
                }
                '*'{ respond step, [status: OK] }
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

            def step = stepService.read(id)
            if (step.project.id != projectId) {
                notFound()
                return
            }

            try {
                stepService.delete(id)
            } catch (Exception ignored) {
                flash.error = "An issue occurred when attempting to delete the Step"
                redirect uri: "/project/${step.project.id}/step/show/${step.id}"
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'step.label', default: 'Step'), id])
                    redirect uri: "/project/${projectId}/steps"
                }
                '*' { render status: NO_CONTENT }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * searches for steps by name
     */
    @Secured("ROLE_BASIC")
    def search(Long projectId, String q) {

        def project = projectService.read(projectId)
        if (project == null) {
            notFound()
            return
        }
        params.max = 7
        def searchResult = stepService.findAllInProjectByName(project, q, params)
        respond searchResult.results, formats: ['json']
    }

    /**
     * gets related steps for the supplied step
     */
    @Secured("ROLE_BASIC")
    def getRelatedSteps(Long projectId, Long stepId) {

        def project = projectService.read(projectId)
        if (stepId == null || project == null) {
            notFound()
            return
        }
        def steps = stepService.getRelatedSteps(stepId)
        respond steps, formats: ['json']
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
