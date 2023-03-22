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

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * lists all projects or perform search
     * if [params.isSearch = true]
     * /projects
     */
    @Secured("ROLE_READ_ONLY")
    def projects(Integer max) {

        if(!params.activePage) {
            params.activePage = "1"
        }
        if(!params.start) {
            params.start = "1"
        }

        def active = params.activePage as int
        def start = params.start as int

        if(!params.isSearch) { // load view
            params.max = Math.min(max ?: 10, 100)
            def projects = projectService.list(params)
            def count = projectService.count()
            def links = getLinks(count, active)

            respond projects, model:[projectCount: count, pageLinks: links, firstPage: getFirstLink(active),
                                     previousPage: getPreviousLink(active), nextPage: getNextLink(count, active)]

        } else { // perform search
            withForm {
                def projects = projectService.findAllByNameIlike(params.name)
                def count = projects.size()
                def links = getLinks(count, active)
                render view:'projects', model: [projectList: projects, projectCount: count, pageLinks: links,
                                                firstPage: getFirstLink(active), previousPage: getPreviousLink(active),
                                                nextPage: getNextLink(count, active)]
            }.invalidToken {
                error()
            }
        }
    }

    //https://github.com/grails/grails-gsp/blob/5.3.x/grails-plugin-gsp/src/main/groovy/org/grails/plugins/web/taglib/UrlMappingTagLib.groovy
    private String getFirstLink(int activePage) {
        return activePage > 3 ? "/projects?offset=0&max=10&activePage=1" : ""
    }

    private String getPreviousLink(int activePage) {
        def num = activePage - 1
        return num == 0 ? "" : "/projects?offset=${(num - 1) * 10}&max=10&activePage=${num}"
    }

    private String getNextLink(Long count, int activePage) {
        def num = activePage + 1
        def last = Math.ceil(count / 10) as int
        return num >= last ? "" : "/projects?offset=${(activePage) * 10}&max=10&activePage=${num}"
    }

    private List<String> getLinks(Long count, int active) {
        def links = []
        if(count > 10) {
            def num = Math.ceil(count / 10) as int
            def max = num >= 5 ? 5 : num
            def offset
            def i = 1;
            while(i < max) {
                offset = active * 10
                links.add("/projects?offset=${offset}&max=10&activePage=${active + 1}")
                active++
                i++
            }
        }
        return links
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
     * displays the show view with project properties
     * @param projectId
     * @return
     */
    @Secured("ROLE_PROJECT_ADMIN")
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
    def update(Project project, RemovedItems removedItems) {
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
                    redirect controller: "project", action:"projects", method:"GET"
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
