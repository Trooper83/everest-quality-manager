package com.everlution

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class BugController {

    BugService bugService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond bugService.list(params), model: [bugCount: bugService.count()]
    }

    def show(Long id) {
        respond bugService.get(id), view: "show"
    }

    def create() {
        respond new Bug(params)
    }

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

    def edit(Long id) {
        respond bugService.get(id), view: "edit"
    }

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
