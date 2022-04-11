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
    def testGroups(Long projectId) {
        def project = projectService.get(projectId)
        if (project == null) {
            notFound()
            return
        }
        def testGroups = testGroupService.findAllByProject(project)
        respond testGroups, model: [testGroupCount: testGroups.size(), project: project], view: 'testGroups'
    }

    /**
     * displays the show view with test group data
     * /testGroup/show/${id}
     * @param id - id of the group
     * @return - the group to show
     */
    @Secured("ROLE_READ_ONLY")
    def show(Long id) {
        respond testGroupService.get(id), view: 'show'
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
        if (testGroup == null) {
            notFound()
            return
        }

        try {
            testGroupService.save(testGroup)
        } catch (ValidationException e) {
            respond testGroup.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'testGroup.label', default: 'TestGroup'), testGroup.id])
                redirect uri: "/project/${testGroup.project.id}/testGroup/show/${testGroup.id}"
            }
            '*' { respond testGroup, [status: CREATED] }
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
    def update(TestGroup testGroup) {
        if (testGroup == null) {
            notFound()
            return
        }

        try {
            testGroupService.save(testGroup)
        } catch (ValidationException e) {
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
    }

    /**
     * deletes a test group
     * @param id - id of the group to delete
     */
    @Secured("ROLE_BASIC")
    def delete(Long id, Long projectId) {
        if (id == null || projectId == null) {
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
    }

    /**
     * displays view when notFound (404)
     */
    protected void notFound() {
        request.withFormat {
            '*'{ render status: NOT_FOUND }
        }
    }
}
