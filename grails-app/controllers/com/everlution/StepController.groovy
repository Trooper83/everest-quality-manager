package com.everlution

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class StepController {

    ProjectService projectService
    IStepService stepService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

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

    @Secured("ROLE_READ_ONLY")
    def show(Long id) {
        respond stepService.get(id)
    }

    def create() {
        respond new Step(params)
    }

    def save(Step step) {
        if (step == null) {
            notFound()
            return
        }

        try {
            stepService.save(step)
        } catch (ValidationException e) {
            respond step.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'step.label', default: 'Step'), step.id])
                redirect step
            }
            '*' { respond step, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond stepService.get(id)
    }

    def update(Step step) {
        if (step == null) {
            notFound()
            return
        }

        try {
            stepService.save(step)
        } catch (ValidationException e) {
            respond step.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'step.label', default: 'Step'), step.id])
                redirect step
            }
            '*'{ respond step, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        stepService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'step.label', default: 'Step'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'step.label', default: 'Step'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
