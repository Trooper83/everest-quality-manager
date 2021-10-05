package com.everlution

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class BugController {

    BugService bugService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * lists all bugs
     * /bug/index
     * @param max - maximum bugs to retrieve
     * @return - list of bugs
     */
    @Secured("ROLE_READ_ONLY")
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond bugService.list(params), model: [bugCount: bugService.count()]
    }

    /**
     * displays the show view with bug data
     * /bug/show/${id}
     * @param id - id of the bug
     * @return - the bug to show
     */
    @Secured("ROLE_READ_ONLY")
    def show(Long id) {
        respond bugService.get(id), view: "show"
    }

    /**
     * displays the create bug view
     * /bug/create
     */
    @Secured("ROLE_BASIC")
    def create() {
        respond new Bug(params)
    }

    /**
     * saves a bug instance
     * @param bug - the bug to save
     */
    @Secured("ROLE_BASIC")
    def save(Bug bug) {
        if (bug == null) {
            notFound()
            return
        }

        try {
            bugService.save(bug)
        } catch (ValidationException e) {
            respond bug.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'bug.label', default: 'Bug'), bug.id])
                redirect bug
            }
            '*' { respond bug, [status: CREATED] }
        }
    }

    /**
     * displays the edit view
     * /bug/edit/${id}
     * @param id - id of the bug
     */
    @Secured("ROLE_BASIC")
    def edit(Long id) {
        respond bugService.get(id), view: "edit"
    }

    /**
     * updates a bugs data
     * @param bug - bug to update
     */
    @Secured("ROLE_BASIC")
    def update(Bug bug) {
        if (bug == null) {
            notFound()
            return
        }

        try {
            bugService.save(bug)
        } catch (ValidationException e) {
            respond bug.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'bug.label', default: 'Bug'), bug.id])
                redirect bug
            }
            '*'{ respond bug, [status: OK] }
        }
    }

    /**
     * deletes a bug
     * @param id - id of the bug to delete
     */
    @Secured("ROLE_BASIC")
    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        bugService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'bug.label', default: 'Bug'), id])
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
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'bug.label', default: 'Bug'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
