package com.everlution

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class TestGroupController {

    ProjectService projectService
    TestGroupService testGroupService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * lists all groups
     * /testGroup/index
     * @param max - maximum groups to retrieve
     * @return - list of groups
     */
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond testGroupService.list(params), model:[testGroupCount: testGroupService.count()]
    }

    /**
     * displays the show view with test group data
     * /testGroup/show/${id}
     * @param id - id of the group
     * @return - the group to show
     */
    def show(Long id) {
        respond testGroupService.get(id), view: 'show'
    }

    /**
     * displays the create test group view
     * /testGroup/create
     */
    def create() {
        respond new TestGroup(params), model: [projects: projectService.list()]
    }

    /**
     * saves a test group instance
     * @param testGroup - the test group to save
     */
    def save(TestGroup testGroup) {
        if (testGroup == null) {
            notFound()
            return
        }

        try {
            testGroupService.save(testGroup)
        } catch (ValidationException e) {
            respond testGroup.errors, view:'create', model: [projects: projectService.list()]
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'testGroup.label', default: 'TestGroup'), testGroup.id])
                redirect testGroup
            }
            '*' { respond testGroup, [status: CREATED] }
        }
    }

    /**
     * displays the edit view
     * /testGroup/edit/${id}
     * @param id - id of the group
     */
    def edit(Long id) {
        respond testGroupService.get(id), view: 'edit'
    }

    /**
     * updates a groups data
     * @param testGroup - group to update
     */
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
                redirect testGroup
            }
            '*'{ respond testGroup, [status: OK] }
        }
    }

    /**
     * deletes a test group
     * @param id - id of the group to delete
     */
    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        testGroupService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'testGroup.label', default: 'TestGroup'), id])
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
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'testGroup.label', default: 'TestGroup'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
