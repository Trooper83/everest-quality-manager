package com.everlution.controllers

import com.everlution.domains.Person
import com.everlution.services.project.ProjectService
import com.everlution.domains.ReleasePlan
import com.everlution.services.releaseplan.ReleasePlanService
import com.everlution.domains.TestCycle
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class ReleasePlanController {

    ProjectService projectService
    ReleasePlanService releasePlanService
    SpringSecurityService springSecurityService

    static allowedMethods = [addTestCycle: "POST", save: "POST", update: "PUT", delete: "DELETE",
                                releasePlans: ["GET", "POST"], create: "GET", edit: "GET", show: "GET"]

    /**
     * adds a new testCycle
     * @param testCycle
     */
    @Secured("ROLE_BASIC")
    def addTestCycle(ReleasePlan releasePlan, TestCycle testCycle) {
        withForm {
            if (releasePlan == null || testCycle == null) {
                notFound()
                return
            }

            try {
                releasePlanService.addTestCycle(releasePlan, testCycle)
            } catch (ValidationException ignored) {
                respond testCycle.errors, view: 'create'
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'testCycle.label', default: 'TestCycle'), testCycle.id])
                    redirect uri: "/project/${releasePlan.project.id}/releasePlan/show/${releasePlan.id}"
                }
                '*' { respond testCycle, [status: CREATED] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * lists all plans
     * /releasePlan/index
     * @return - list of release plans
     */
    @Secured("ROLE_READ_ONLY")
    def releasePlans(Long projectId, Integer max) {

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
            searchResults = releasePlanService.findAllByProject(project, params)

        } else { // perform search
            searchResults = releasePlanService.findAllInProjectByName(project, params.searchTerm, params)
        }
        respond searchResults.results, model: [releasePlanCount: searchResults.count, project: project], view: 'releasePlans'
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
    def create(Long projectId) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }
        respond new ReleasePlan(params), model: [project: project]
    }

    /**
     * saves a release plan instance
     * @param releasePlan - the plan to save
     */
    @Secured("ROLE_BASIC")
    def save(ReleasePlan releasePlan) {
        withForm {
            if (releasePlan == null) {
                notFound()
                return
            }

            def person = springSecurityService.getCurrentUser() as Person
            releasePlan.person = person

            try {
                releasePlanService.save(releasePlan)
            } catch (ValidationException ignored) {
                def project = projectService.read(releasePlan.project.id)
                respond releasePlan.errors, view:'create', model: [ project: project ]
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'releasePlan.label', default: 'ReleasePlan'), releasePlan.id])
                    redirect uri: "/project/${releasePlan.project.id}/releasePlan/show/${releasePlan.id}"
                }
                '*' { respond releasePlan, [status: CREATED] }
            }
        }.invalidToken {
            error()
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
    def update(ReleasePlan releasePlan, Long projectId) {
        withForm {
            if (releasePlan == null || projectId == null) {
                notFound()
                return
            }
            def planProjectId = releasePlan.project.id
            if (projectId != planProjectId) {
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
                    redirect uri: "/project/${releasePlan.project.id}/releasePlan/show/${releasePlan.id}"
                }
                '*'{ respond releasePlan, [status: OK] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * deletes a release plan
     * @param id - id of the plan to delete
     */
    @Secured("ROLE_BASIC")
    def delete(Long id, Long projectId) {
        withForm {
            if (id == null || projectId == null) {
                notFound()
                return
            }

            def plan = releasePlanService.read(id)
            if (plan.project.id != projectId) {
                notFound()
                return
            }

            releasePlanService.delete(id)

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'releasePlan.label', default: 'ReleasePlan'), id])
                    redirect uri: "/project/${projectId}/releasePlans"
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
