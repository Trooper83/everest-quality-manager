package com.everlution

import com.everlution.command.RemovedItems
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import org.springframework.dao.DataIntegrityViolationException

import static org.springframework.http.HttpStatus.*

class ProjectController {

    BugService bugService
    ProjectService projectService
    ReleasePlanService releasePlanService
    ScenarioService scenarioService
    TestCaseService testCaseService

    static allowedMethods = [getProjectItems: "GET", save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * lists all projects or perform search
     * if [params.isSearch = true]
     * /projects
     */
    @Secured("ROLE_READ_ONLY")
    def projects(Integer max) {

        if(!params.isSearch) { // load view
            params.max = Math.min(max ?: 10, 100)
            def projects = projectService.list(params)

            if(projects.size() == 0) {
                flash.message = 'There are no projects in your organization'
            }
            respond projects, model:[projectCount: projectService.count()]

        } else { // perform search
            def projects = projectService.findAllByNameIlike(params.name)
            if(projects.size() == 0) {
                flash.message = "No projects were found using search term: '${params.name}'"
            }
            render view:'projects', model: [projectList: projects, projectCount: projects.size()]
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
        def bugCount = bugService.findAllByProject(project).size()
        def testCaseCount = testCaseService.findAllByProject(project).size()
        def scenarioCount = scenarioService.findAllByProject(project).size()
        def releasePlans = releasePlanService.findAllByProject(project)
        def plans = getPlans(releasePlans)
        respond project, view: "home", model: [testCaseCount: testCaseCount, scenarioCount: scenarioCount,
                bugCount: bugCount, nextRelease: plans.nextRelease, previousRelease: plans.previousRelease]
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
        } catch (ValidationException ignored) {
            respond project.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'project.label', default: 'Project'), project.id])
                redirect controller: "project", action: "home", id: project.id
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
    def edit(Long projectId) {
        respond projectService.get(projectId), view: "edit"
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
        } catch (ValidationException e) {
            def p = projectService.read(project.id)
            p.errors = e.errors
            render view:'edit', model: [project: p]
            return
        } catch (DataIntegrityViolationException ignored) {
            flash.error = 'Removed entity has associated items and cannot be deleted'
            def p = projectService.read(params.id)
            render view:'edit', model: [project: p]
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'project.label', default: 'Project'), project.id])
                redirect controller: "project", action: "home", id: project.id
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

        try {
            projectService.delete(id)
        } catch (DataIntegrityViolationException ignored) {
            def project = projectService.read(id)
            flash.error = 'Project has associated items and cannot be deleted'
            respond project, view:'home'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'project.label', default: 'Project'), id])
                redirect controller: "project", action:"projects", method:"GET"
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
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    /**
     * gets the next and previous release plans
     */
    private LinkedHashMap<String, ReleasePlan> getPlans(List<ReleasePlan> releasePlans) {

        def now = new Date()

        List<ReleasePlan> previous = List.copyOf(releasePlans)
        def previousRelease = previous
                .findAll {it.releaseDate != null & it.releaseDate < now }
                .findAll {it.status == 'Released' }
                .max { it.releaseDate }


        List<ReleasePlan> next = List.copyOf(releasePlans)
        def nextRelease = next
                .findAll {it.plannedDate != null & it.plannedDate > now }
                .findAll {it.status != 'Released' }
                .min { it.plannedDate }
        return [ nextRelease: nextRelease, previousRelease: previousRelease ]
    }
}
