package com.everlution

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class ReleasePlanController {

    ProjectService projectService
    ReleasePlanService releasePlanService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * lists all projects
     * /releasePlan/index
     * @param max - maximum plans to retrieve
     * @return - list of release plans
     */
    @Secured("ROLE_READ_ONLY")
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond releasePlanService.list(params), model:[releasePlanCount: releasePlanService.count()]
    }

    /**
     * displays the show view with release plan data
     * /releasePlan/show/${id}
     * @param id - id of the plan
     * @return - the plan to show
     */
    @Secured("ROLE_READ_ONLY")
    def show(Long id) {
        respond releasePlanService.get(id), view: 'show'
    }

    /**
     * displays the create release plan view
     * /releasePlan/create
     */
    @Secured("ROLE_BASIC")
    def create() {
        respond new ReleasePlan(params), model: [projects: projectService.list()]
    }

    /**
     * saves a release plan instance
     * @param project - the plan to save
     */
    @Secured("ROLE_BASIC")
    def save(ReleasePlan releasePlan) {
        if (releasePlan == null) {
            notFound()
            return
        }

        try {
            releasePlanService.save(releasePlan)
        } catch (ValidationException e) {
            respond releasePlan.errors, view:'create', model: [projects: projectService.list()]
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'releasePlan.label', default: 'ReleasePlan'), releasePlan.id])
                redirect releasePlan
            }
            '*' { respond releasePlan, [status: CREATED] }
        }
    }

    /**
     * displays the edit view
     * /releasePlan/edit/${id}
     * @param id - id of the plan
     */
    @Secured("ROLE_BASIC")
    def edit(Long id) {
        respond releasePlanService.get(id), view: 'edit'
    }

    /**
     * updates a plans data
     * @param releasePlan - plan to update
     */
    @Secured("ROLE_BASIC")
    def update(ReleasePlan releasePlan) {
        if (releasePlan == null) {
            notFound()
            return
        }

        try {
            releasePlanService.save(releasePlan)
        } catch (ValidationException e) {
            respond releasePlan.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'releasePlan.label', default: 'ReleasePlan'), releasePlan.id])
                redirect releasePlan
            }
            '*'{ respond releasePlan, [status: OK] }
        }
    }

    /**
     * deletes a release plan
     * @param id - id of the plan to delete
     */
    @Secured("ROLE_BASIC")
    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        releasePlanService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'releasePlan.label', default: 'ReleasePlan'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    /**
     * displays view when notFound (404)
     */
    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'releasePlan.label', default: 'ReleasePlan'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
