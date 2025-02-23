package com.manager.quality.everest.controllers

import com.manager.quality.everest.services.automatedtest.AutomatedTestService
import com.manager.quality.everest.services.bug.BugService
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.services.releaseplan.ReleasePlanService
import com.manager.quality.everest.services.scenario.ScenarioService
import com.manager.quality.everest.services.testcase.TestCaseService
import com.manager.quality.everest.services.testresult.TestResultService
import com.manager.quality.everest.services.testrun.TestRunService
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import org.springframework.dao.DataIntegrityViolationException

import static org.springframework.http.HttpStatus.*

class ProjectController {

    AutomatedTestService automatedTestService
    BugService bugService
    ProjectService projectService
    ReleasePlanService releasePlanService
    ScenarioService scenarioService
    TestCaseService testCaseService
    TestRunService testRunService
    TestResultService testResultService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", projects: ["GET", "POST"],
                                create: "GET", show: "GET", edit: "GET", home: "GET"]

    /**
     * lists all projects or perform search
     * if [params.isSearch = true]
     * /projects
     */
    @Secured("ROLE_READ_ONLY")
    def projects(Integer max) {
        params.max = Math.min(max ?: 25, 100)
        if(!params.sort) {
            params.sort = 'name'
            params.order = 'asc'
        }
        if(!params.isSearch) { // load view
            def projects = projectService.list(params)
            def count = projectService.count()
            respond projects, model:[projectCount: count]

        } else { // perform search
            def results = projectService.findAllByNameIlike(params.searchTerm, params)
            render view:'projects', model: [projectList: results.results, projectCount: results.count]
        }
    }

    /**
     * displays the home view with project data
     * /project/${id}/home
     * @param id - id of the project
     * @return - the project to show
     */
    @Secured("ROLE_READ_ONLY")
    def home(Long projectId) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }

        // bugs
        def recentBugs = bugService.findAllByProject(project, [max:10, sort: 'dateCreated', order: 'desc']).results
        def bugCount = bugService.countByProjectAndStatus(project, "Open")
        // automated tests
        def autoTestCount = automatedTestService.countByProject(project)
        // release plans
        def releasePlans = releasePlanService.getPlansByStatus(project)
        // test cases
        def testCasesCount = testCaseService.countByProject(project)
        // scenarios
        def scenarioCount = scenarioService.countByProject(project)

        respond project, view: "home", model: [bugCount: bugCount, next: releasePlans.next,
                                               released: releasePlans.released, current: releasePlans.inProgress,
                                               automatedTestCount: autoTestCount, recentBugs: recentBugs,
                                               testCaseCount: testCasesCount, scenarioCount: scenarioCount]
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
     * displays the show view with project properties
     * @param projectId
     * @return
     */
    @Secured("ROLE_BASIC")
    def show(Long projectId) {
        respond projectService.get(projectId), view: "show"
    }

    /**
     * saves a project instance
     * @param project - the project to save
     */
    @Secured("ROLE_PROJECT_ADMIN")
    def save(Project project) {
        withForm {
            if (project == null) {
                notFound()
                return
            }

            try {
                projectService.save(project)
            } catch (ValidationException ignored) {
                respond project.errors, view:'create'
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'project.label', default: 'Project'), project.id])
                    redirect controller: "project", action: "show", id: project.id
                }
                '*' { respond project, [status: CREATED] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * displays the edit view
     * /project/edit/${id}
     * @param id - id of the project
     */
    @Secured("ROLE_PROJECT_ADMIN")
    def edit(Long projectId) {
        respond projectService.get(projectId), view: "edit"
    }

    /**
     * updates a projects data
     * @param project - project to update
     */
    @Secured("ROLE_PROJECT_ADMIN")
    def update(Project project, com.manager.quality.everest.controllers.command.RemovedItems removedItems) {
        withForm {
            if (project == null) {
                notFound()
                return
            }

            try {
                projectService.saveUpdate(project, removedItems)
            } catch (ValidationException e) {
                def p = projectService.read(project.id)
                p.errors = e.errors
                params.projectId = p.id
                render view:'edit', model: [project: p]
                return
            } catch (DataIntegrityViolationException ignored) {
                flash.error = 'Removed entity has associated items and cannot be deleted'
                def p = projectService.read(params.id)
                params.projectId = p.id
                render view:'edit', model: [project: p]
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'project.label', default: 'Project'), project.id])
                    redirect controller: "project", action: "show", id: project.id
                }
                '*'{ respond project, [status: OK] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * deletes a project
     * @param id - id of the project to delete
     */
    @Secured("ROLE_PROJECT_ADMIN")
    def delete(Long id) {
        withForm {
            if (id == null) {
                notFound()
                return
            }

            try {
                projectService.delete(id)
            } catch (DataIntegrityViolationException ignored) {
                def project = projectService.read(id)
                flash.error = 'Project has associated items and cannot be deleted'
                params.projectId = project.id
                respond project, view: 'show'
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'project.label', default: 'Project'), id])
                    redirect uri: "/projects", method:"GET"
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
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])
                redirect action: "index", method: "GET"
            }
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
