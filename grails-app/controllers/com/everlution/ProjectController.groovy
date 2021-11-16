package com.everlution

import com.everlution.command.RemovedItems
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import org.springframework.dao.DataIntegrityViolationException

import static org.springframework.http.HttpStatus.*

class ProjectController {

    ProjectService projectService

    static allowedMethods = [getProjectItems: "GET", save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * lists all projects
     * /project/index
     * @param max - maximum projects to retrieve
     * @return - list of projects
     */
    @Secured("ROLE_PROJECT_ADMIN")
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond projectService.list(params), model:[projectCount: projectService.count()]
    }

    /**
     * displays the show view with project data
     * /project/show/${id}
     * @param id - id of the project
     * @return - the project to show
     */
    @Secured("ROLE_PROJECT_ADMIN")
    def show(Long id) {
        respond projectService.get(id), view: "show"
    }

    /**
     * displays the create project view
     * /project/create
     */
    @Secured("ROLE_PROJECT_ADMIN")
    def create() {
        respond new Project(params)
    }

    /**
     * saves a project instance
     * @param project - the project to save
     */
    @Secured("ROLE_PROJECT_ADMIN")
    def save(Project project) {
        if (project == null) {
            notFound()
            return
        }

        try {
            projectService.save(project)
        } catch (ValidationException e) {
            respond project.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'project.label', default: 'Project'), project.id])
                redirect project
            }
            '*' { respond project, [status: CREATED] }
        }
    }

    /**
     * displays the edit view
     * /project/edit/${id}
     * @param id - id of the project
     */
    @Secured("ROLE_PROJECT_ADMIN")
    def edit(Long id) {
        respond projectService.get(id), view: "edit"
    }

    /**
     * updates a projects data
     * @param project - project to update
     */
    @Secured("ROLE_PROJECT_ADMIN")
    def update(Project project, RemovedItems removedItems) {
        if (project == null) {
            notFound()
            return
        }

        try {
            projectService.saveUpdate(project, removedItems)
        } catch (ValidationException ignored) {
            respond project.errors, view:'edit'
            return
        } catch (DataIntegrityViolationException ignored) {
            flash.error = 'Removed entity has associated items and cannot be deleted'
            project = Project.read(params.id)
            respond project, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'project.label', default: 'Project'), project.id])
                redirect project
            }
            '*'{ respond project, [status: OK] }
        }
    }

    /**
     * deletes a project
     * @param id - id of the project to delete
     */
    @Secured("ROLE_PROJECT_ADMIN")
    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        projectService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'project.label', default: 'Project'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    /**
     * gets the areas of a project
     * @param project - the project
     * @return - a list of the areas
     */
    @Secured("ROLE_BASIC")
    def getProjectItems(Project project) {
        if (project == null) {
            notFound()
            return
        }
        respond (["areas": project.areas, "environments": project.environments])
    }

    /**
     * displays view when notFound (404)
     */
    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
