package com.everlution.test

import com.everlution.BugController
import com.everlution.ProjectController
import com.everlution.ReleasePlanController
import com.everlution.ScenarioController
import com.everlution.TestCaseController
import com.everlution.TestCycleController
import com.everlution.TestGroupController
import com.everlution.TestIterationController
import com.everlution.UrlMappings
import grails.plugin.springsecurity.LoginController
import grails.testing.web.UrlMappingsUnitTest
import spock.lang.Specification

class UrlMappingsSpec extends Specification implements UrlMappingsUnitTest<UrlMappings> {

    void "test forward error mappings"() {
        expect:
        verifyForwardUrlMapping(500, view: 'error')
        verifyForwardUrlMapping(404, view: 'notFound')
    }

    void "verify login forward and reverse mappings"() {
        mockController(LoginController)

        expect:
        verifyUrlMapping("/login/auth", controller: 'login', action: 'auth')
    }

    void "verify test case forward and reverse mappings"() {
        mockController(TestCaseController)

        expect:
        verifyUrlMapping("/testCase/index", controller: 'testCase', action: 'index')
        verifyUrlMapping("/testCase/create", controller: 'testCase', action: 'create')
        verifyUrlMapping("/testCase/save", controller: 'testCase', action: 'save')
        verifyUrlMapping("/testCase/update", controller: 'testCase', action: 'update')
        verifyUrlMapping("/testCase/show/123", controller: 'testCase', action: 'show') {
            id = 123
        }
        verifyUrlMapping("/testCase/edit/123", controller: 'testCase', action: 'edit') {
            id = 123
        }
        verifyUrlMapping("/testCase/delete/123", controller: 'testCase', action: 'delete') {
            id = 123
        }
    }

    void "verify project forward and reverse mappings"() {
        mockController(ProjectController)

        expect:
        verifyUrlMapping("/projects", controller: 'project', action: 'projects')
        verifyUrlMapping("/project/123/home", controller: 'project', action: 'home') {
            projectId = 123
        }
        verifyUrlMapping("/project/create", controller: 'project', action: 'create')
        verifyUrlMapping("/project/save", controller: 'project', action: 'save')
        verifyUrlMapping("/project/update", controller: 'project', action: 'update')
        verifyUrlMapping("/project/show/123", controller: 'project', action: 'show') {
            id = 123
        }
        verifyUrlMapping("/project/edit/123", controller: 'project', action: 'edit') {
            id = 123
        }
        verifyUrlMapping("/project/delete/123", controller: 'project', action: 'delete') {
            id = 123
        }
        verifyUrlMapping("/project/getProjectItems", controller: 'project', action: 'getProjectItems')
    }

    void "verify bug forward and reverse mappings"() {
        mockController(BugController)

        expect:
        verifyUrlMapping("/project/123/bug/create", controller: 'bug', action: 'create') {
            projectId = 123
        }
        verifyUrlMapping("/project/123/bugs", controller: 'bug', action: 'bugs') {
            projectId = 123
        }
        verifyUrlMapping("/bug/save", controller: 'bug', action: 'save')
        verifyUrlMapping("/bug/update", controller: 'bug', action: 'update')
        verifyUrlMapping("/project/123/bug/show/123", controller: 'bug', action: 'show') {
            id = 123
            projectId = 123
        }
        verifyUrlMapping("/project/123/bug/edit/123", controller: 'bug', action: 'edit') {
            id = 123
            projectId = 123
        }
        verifyUrlMapping("/bug/delete/123", controller: 'bug', action: 'delete') {
            id = 123
        }
    }

    void "verify scenario forward and reverse mappings"() {
        mockController(ScenarioController)

        expect:
        verifyUrlMapping("/scenario/index", controller: 'scenario', action: 'index')
        verifyUrlMapping("/scenario/create", controller: 'scenario', action: 'create')
        verifyUrlMapping("/scenario/save", controller: 'scenario', action: 'save')
        verifyUrlMapping("/scenario/update", controller: 'scenario', action: 'update')
        verifyUrlMapping("/scenario/show/123", controller: 'scenario', action: 'show') {
            id = 123
        }
        verifyUrlMapping("/scenario/edit/123", controller: 'scenario', action: 'edit') {
            id = 123
        }
        verifyUrlMapping("/scenario/delete/123", controller: 'scenario', action: 'delete') {
            id = 123
        }
    }

    void "verify release plan forward and reverse mappings"() {
        mockController(ReleasePlanController)

        expect:
        verifyUrlMapping("/releasePlan/index", controller: 'releasePlan', action: 'index')
        verifyUrlMapping("/releasePlan/create", controller: 'releasePlan', action: 'create')
        verifyUrlMapping("/releasePlan/save", controller: 'releasePlan', action: 'save')
        verifyUrlMapping("/releasePlan/update", controller: 'releasePlan', action: 'update')
        verifyUrlMapping("/releasePlan/show/123", controller: 'releasePlan', action: 'show') {
            id = 123
        }
        verifyUrlMapping("/releasePlan/edit/123", controller: 'releasePlan', action: 'edit') {
            id = 123
        }
        verifyUrlMapping("/releasePlan/delete/123", controller: 'releasePlan', action: 'delete') {
            id = 123
        }
    }

    void "verify test group forward and reverse mappings"() {
        mockController(TestGroupController)

        expect:
        verifyUrlMapping("/testGroup/index", controller: 'testGroup', action: 'index')
        verifyUrlMapping("/testGroup/create", controller: 'testGroup', action: 'create')
        verifyUrlMapping("/testGroup/save", controller: 'testGroup', action: 'save')
        verifyUrlMapping("/testGroup/update", controller: 'testGroup', action: 'update')
        verifyUrlMapping("/testGroup/show/123", controller: 'testGroup', action: 'show') {
            id = 123
        }
        verifyUrlMapping("/testGroup/edit/123", controller: 'testGroup', action: 'edit') {
            id = 123
        }
        verifyUrlMapping("/testGroup/delete/123", controller: 'testGroup', action: 'delete') {
            id = 123
        }
    }

    void "verify test cycle forward and reverse mappings"() {
        mockController(TestCycleController)

        expect:
        verifyUrlMapping("/testCycle/create", controller: 'testCycle', action: 'create')
        verifyUrlMapping("/testCycle/save", controller: 'testCycle', action: 'save')
        verifyUrlMapping("/testCycle/show/123", controller: 'testCycle', action: 'show') {
            id = 123
        }
    }

    void "verify test iteration forward and reverse mappings"() {
        mockController(TestIterationController)

        expect:
        verifyUrlMapping("/testIteration/show/123", controller: 'testIteration', action: 'show') {
            id = 123
        }
        verifyUrlMapping("/testIteration/execute/123", controller: 'testIteration', action: 'execute') {
            id = 123
        }
    }
}
