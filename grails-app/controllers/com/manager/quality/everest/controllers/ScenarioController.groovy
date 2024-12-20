package com.manager.quality.everest.controllers

import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.Scenario
import com.manager.quality.everest.services.scenario.ScenarioService
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class ScenarioController {

    ProjectService projectService
    ScenarioService scenarioService
    SpringSecurityService springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", scenarios: ["GET", "POST"],
                                create: "GET", show: "GET", edit: "GET"]

    /**
     * lists all scenarios
     * /scenarios
     * @return - list of scenarios
     */
    @Secured("ROLE_READ_ONLY")
    def scenarios(Long projectId, Integer max) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }

        params.max = Math.min(max ?: 25, 100)
        if(!params.sort) {
            params.sort = 'dateCreated'
            params.order = 'desc'
        }
        def searchResults
        if(!params.isSearch) { // load view
            searchResults = scenarioService.findAllByProject(project, params)

        } else { // perform search
            searchResults = scenarioService.findAllInProjectByName(project, params.searchTerm, params)
        }
        respond searchResults.results, model: [scenarioCount: searchResults.count, project: project], view: 'scenarios'
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
    def create(Long projectId) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }
        respond new Scenario(params), model: [project: project]
    }

    /**
     * saves a scenario
     * @param scenario - the scenario to save
     */
    @Secured("ROLE_BASIC")
    def save(Scenario scenario) {
        withForm {
            if (scenario == null) {
                notFound()
                return
            }

            def person = springSecurityService.getCurrentUser() as Person
            scenario.person = person

            try {
                scenarioService.save(scenario)
            } catch (ValidationException ignored) {
                def project = projectService.read(scenario.project.id)
                respond scenario.errors, view:'create', model: [ project: project ]
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'scenario.label', default: 'Scenario'), scenario.id])
                    redirect uri: "/project/${scenario.project.id}/scenario/show/${scenario.id}"
                }
                '*' { respond scenario, [status: CREATED] }
            }
        }.invalidToken {
            error()
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
    def update(Scenario scenario, Long projectId) {
        withForm {
            if (scenario == null|| projectId == null) {
                notFound()
                return
            }

            def scnProjectId = scenario.project.id
            if (projectId != scnProjectId) {
                notFound()
                return
            }

            try {
                scenarioService.save(scenario)
            } catch (ValidationException e) {
                def scn = scenarioService.read(scenario.id)
                scn.errors = e.errors
                render view:'edit', model: [scenario: scn]
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'scenario.label', default: 'Scenario'), scenario.id])
                    redirect uri: "/project/${scenario.project.id}/scenario/show/${scenario.id}"
                }
                '*'{ respond scenario, [status: OK] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * deletes a scenario
     * @param id - id of the scenario to delete
     */
    @Secured("ROLE_BASIC")
    def delete(Long id, Long projectId) {
        withForm {
            if (id == null || projectId == null) {
                notFound()
                return
            }
            def scenario = scenarioService.read(id)
            if (scenario.project.id != projectId) {
                notFound()
                return
            }

            scenarioService.delete(id)

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'scenario.label', default: 'Scenario'), id])
                    redirect uri: "/project/${projectId}/scenarios"
                }
                '*'{ render status: NO_CONTENT }
            }
        }.invalidToken {
            error()
        }
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
