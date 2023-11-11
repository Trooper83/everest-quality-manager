package com.everlution

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class TestGroupController {

    ProjectService projectService
    TestGroupService testGroupService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * lists all groups
     * /testGroups
     * @return - list of groups
     */
    @Secured("ROLE_READ_ONLY")
    def testGroups(Long projectId, Integer max) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }

        params.max = Math.min(max ?: 25, 100)
        def testGroupResults
        if(!params.isSearch) { // load view
            testGroupResults = testGroupService.findAllByProject(project, params)

        } else { // perform search
            testGroupResults = testGroupService.findAllInProjectByName(project, params.name, params)
        }
        respond testGroupResults.results, model: [testGroupCount: testGroupResults.count, project: project], view: 'testGroups'
    }

    /**
     * displays the show view with test group data
     * /testGroup/show/${id}
     * @param id - id of the group
     * @return - the group to show
     */
    @Secured("ROLE_READ_ONLY")
    def show(Long id, Integer max) {
        params.max = Math.min(max ?: 25, 100)
        def group = testGroupService.getWithPaginatedTests(id, params)
        respond group.testGroup, view: 'show', model: [tests: group.tests]
    }

    /**
     * displays the create test group view
     * /testGroup/create
     */
    @Secured("ROLE_BASIC")
    def create(Long projectId) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }
        respond new TestGroup(params), model: [project: project], view: 'create'
    }

    /**
     * saves a test group instance
     * @param testGroup - the test group to save
     */
    @Secured("ROLE_BASIC")
    def save(TestGroup testGroup) {
        withForm {
            if (testGroup == null) {
                notFound()
                return
            }

            try {
                testGroupService.save(testGroup)
            } catch (ValidationException ignored) {
                def project = projectService.read(testGroup.project.id)
                respond testGroup.errors, view:'create', model: [ project: project ]
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'testGroup.label', default: 'TestGroup'), testGroup.id])
                    redirect uri: "/project/${testGroup.project.id}/testGroup/show/${testGroup.id}"
                }
                '*' { respond testGroup, [status: CREATED] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * displays the edit view
     * /testGroup/edit/${id}
     * @param id - id of the group
     */
    @Secured("ROLE_BASIC")
    def edit(Long id) {
        respond testGroupService.get(id), view: 'edit'
    }

    /**
     * updates a groups data
     * @param testGroup - group to update
     */
    @Secured("ROLE_BASIC")
    def update(TestGroup testGroup, Long projectId) {
        withForm {
            if (testGroup == null || projectId == null) {
                notFound()
                return
            }
            def groupId = testGroup.project.id
            if (projectId != groupId) {
                notFound()
                return
            }

            try {
                testGroupService.save(testGroup)
            } catch (ValidationException ignored) {
                respond testGroup.errors, view:'edit'
                return
            }

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'testGroup.label', default: 'TestGroup'), testGroup.id])
                    redirect uri: "/project/${testGroup.project.id}/testGroup/show/${testGroup.id}"
                }
                '*'{ respond testGroup, [status: OK] }
            }
        }.invalidToken {
            error()
        }
    }

    /**
     * deletes a test group
     * @param id - id of the group to delete
     */
    @Secured("ROLE_BASIC")
    def delete(Long id, Long projectId) {
        withForm {
            if (id == null || projectId == null) {
                notFound()
                return
            }
            def group = testGroupService.read(id)
            if (group.project.id != projectId) {
                notFound()
                return
            }

            testGroupService.delete(id)

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'testGroup.label', default: 'TestGroup'), id])
                    redirect uri: "/project/${projectId}/testGroups"
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
