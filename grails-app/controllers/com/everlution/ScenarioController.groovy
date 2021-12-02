package com.everlution

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class ScenarioController {

    ProjectService projectService
    ScenarioService scenarioService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * lists all scenarios
     * /scenario/index
     * @param max - maximum number of test cases to retrieve
     * @return - list of scenarios
     */
    @Secured("ROLE_READ_ONLY")
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond scenarioService.list(params), model:[scenarioCount: scenarioService.count()]
    }

    /**
     * shows a single scenario
     * /scenario/show/$id
     * @param id - id of the scenario to display
     * @return - a single scenario
     */
    @Secured("ROLE_READ_ONLY")
    def show(Long id) {
        respond scenarioService.get(id), view: 'show'
    }

    /**
     * display the create scenario view
     * /scenario/create
     * @return - create scenario view
     */
    @Secured("ROLE_BASIC")
    def create() {
        respond new Scenario(params), model: [projects: projectService.list()]
    }

    /**
     * saves a scenario
     * @param scenario - the scenario to save
     */
    @Secured("ROLE_BASIC")
    def save(Scenario scenario) {
        if (scenario == null) {
            notFound()
            return
        }

        try {
            scenarioService.save(scenario)
        } catch (ValidationException e) {
            respond scenario.errors, view:'create', model: [projects: projectService.list()]
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'scenario.label', default: 'Scenario'), scenario.id])
                redirect scenario
            }
            '*' { respond scenario, [status: CREATED] }
        }
    }

    /**
     * displays the edit view
     * /scenario/edit
     * @param id - id of the scenario to edit
     */
    @Secured("ROLE_BASIC")
    def edit(Long id) {
        respond scenarioService.get(id), view: 'edit'
    }

    /**
     * updates a scenario
     * @param scenario - scenario values to update
     */
    @Secured("ROLE_BASIC")
    def update(Scenario scenario) {
        if (scenario == null) {
            notFound()
            return
        }

        try {
            scenarioService.save(scenario)
        } catch (ValidationException e) {
            respond scenario.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'scenario.label', default: 'Scenario'), scenario.id])
                redirect scenario
            }
            '*'{ respond scenario, [status: OK] }
        }
    }

    /**
     * deletes a scenario
     * @param id - id of the scenario to delete
     */
    @Secured("ROLE_BASIC")
    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        scenarioService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'scenario.label', default: 'Scenario'), id])
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
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'scenario.label', default: 'Scenario'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
