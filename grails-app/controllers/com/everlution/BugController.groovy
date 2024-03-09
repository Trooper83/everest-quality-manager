package com.everlution

import com.everlution.command.RemovedItems
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException

import static org.springframework.http.HttpStatus.*

class BugController {

    BugService bugService
    ProjectService projectService
    SpringSecurityService springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * lists all bugs or perform search
     * if [params.isSearch = true]
     * /bugs
     * @return - list of bugs
     */
    @Secured("ROLE_READ_ONLY")
    def bugs(Long projectId, Integer max) {

        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }

        params.max = Math.min(max ?: 25, 100)
        def searchResult
        if(!params.isSearch) { // load view
            searchResult = bugService.findAllByProject(project, params)


        } else { // perform search
            searchResult = bugService.findAllInProjectByName(project, params.searchTerm, params)
        }
        respond searchResult.results, model: [bugCount: searchResult.count, project: project], view: 'bugs'
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
    def create(Long projectId) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }
        respond new Bug(params), model: [project: project], view: 'create'
    }

    /**
     * saves a bug instance
     * @param bug - the bug to save
     */
    @Secured("ROLE_BASIC")
    def save(Bug bug) {
        withForm {
            if (bug == null) {
                notFound()
                return
            }

            def person = springSecurityService.getCurrentUser() as Person
            bug.person = person

            try {
                bugService.save(bug)
            } catch (ValidationException ignored) {
                def project = projectService.read(bug.project.id)
                respond bug.errors, view:'create', model: [ project: project ]
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'bug.label', default: 'Bug'), bug.id])
                    redirect uri: "/project/${bug.project.id}/bug/show/${bug.id}"
                }
                '*' { respond bug, [status: CREATED] }
            }
        }.invalidToken {
            error()
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
    def update(Bug bug, RemovedItems removedItems, Long projectId) {
        withForm {
            if (bug == null || projectId == null) {
                notFound()
                return
            }
            def bugProjectId = bug.project.id
            if (projectId != bugProjectId) {
                notFound()
                return
            }

            try {
                bugService.saveUpdate(bug, removedItems)
            } catch (ValidationException e) {
                def b = bugService.read(bug.id)
                b.errors = e.errors
                render view: 'edit', model: [bug: b]
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'bug.label', default: 'Bug'), bug.id])
                    redirect uri: "/project/${bug.project.id}/bug/show/${bug.id}"
                }
                '*'{ respond bug, [status: OK] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * deletes a bug
     * @param id - id of the bug to delete
     */
    @Secured("ROLE_BASIC")
    def delete(Long id, Long projectId) {
        withForm {
            if (id == null || projectId == null) {
                notFound()
                return
            }
            def bug = bugService.read(id)
            if (bug.project.id != projectId) {
                notFound()
                return
            }
            bugService.delete(id)

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'bug.label', default: 'Bug'), id])
                    redirect uri: "/project/${projectId}/bugs"
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
