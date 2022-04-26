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
        verifyUrlMapping("/testCase/save", controller: 'testCase', action: 'save')
        verifyUrlMapping("/testCase/update", controller: 'testCase', action: 'update')
        verifyUrlMapping("/testCase/delete/123", controller: 'testCase', action: 'delete') {
            id = 123
        }
        verifyUrlMapping("/project/123/testCases", controller: 'testCase', action: 'testCases') {
            projectId = 123
        }
        verifyUrlMapping("/project/123/testCase/create", controller: 'testCase', action: 'create') {
            projectId = 123
        }
        verifyUrlMapping("/project/999/testCase/show/123", controller: 'testCase', action: 'show') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/123/testCase/edit/123", controller: 'testCase', action: 'edit') {
            id = 123
            projectId = 123
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
        verifyUrlMapping("/bug/save", controller: 'bug', action: 'save')
        verifyUrlMapping("/bug/update", controller: 'bug', action: 'update')
        verifyUrlMapping("/project/123/bug/create", controller: 'bug', action: 'create') {
            projectId = 123
        }
        verifyUrlMapping("/project/123/bugs", controller: 'bug', action: 'bugs') {
            projectId = 123
        }
        verifyUrlMapping("/bug/delete/123", controller: 'bug', action: 'delete') {
            id = 123
        }
        verifyUrlMapping("/project/123/bug/show/123", controller: 'bug', action: 'show') {
            id = 123
            projectId = 123
        }
        verifyUrlMapping("/project/123/bug/edit/123", controller: 'bug', action: 'edit') {
            id = 123
            projectId = 123
        }
    }

    void "verify scenario forward and reverse mappings"() {
        mockController(ScenarioController)

        expect:
        verifyUrlMapping("/scenario/save", controller: 'scenario', action: 'save')
        verifyUrlMapping("/scenario/update", controller: 'scenario', action: 'update')
        verifyUrlMapping("/project/123/scenarios", controller: 'scenario', action: 'scenarios') {
            projectId = 123
        }
        verifyUrlMapping("/project/123/scenario/create", controller: 'scenario', action: 'create') {
            projectId = 123
        }
        verifyUrlMapping("/scenario/delete/123", controller: 'scenario', action: 'delete') {
            id = 123
        }
        verifyUrlMapping("/project/123/scenario/show/123", controller: 'scenario', action: 'show') {
            id = 123
            projectId = 123
        }
        verifyUrlMapping("/project/123/scenario/edit/123", controller: 'scenario', action: 'edit') {
            id = 123
            projectId = 123
        }
    }

    void "verify release plan forward and reverse mappings"() {
        mockController(ReleasePlanController)

        expect:
        verifyUrlMapping("/releasePlan/save", controller: 'releasePlan', action: 'save')
        verifyUrlMapping("/releasePlan/update", controller: 'releasePlan', action: 'update')
        verifyUrlMapping("/releasePlan/delete/123", controller: 'releasePlan', action: 'delete') {
            id = 123
        }
        verifyUrlMapping("/project/999/releasePlans", controller: 'releasePlan', action: 'releasePlans') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/releasePlan/create", controller: 'releasePlan', action: 'create') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/releasePlan/show/123", controller: 'releasePlan', action: 'show') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/999/releasePlan/edit/123", controller: 'releasePlan', action: 'edit') {
            id = 123
            projectId = 999
        }
    }

    void "verify test group forward and reverse mappings"() {
        mockController(TestGroupController)

        expect:
        verifyUrlMapping("/testGroup/update", controller: 'testGroup', action: 'update')
        verifyUrlMapping("/testGroup/save", controller: 'testGroup', action: 'save')
        verifyUrlMapping("/testGroup/delete/123", controller: 'testGroup', action: 'delete') {
            id = 123
        }
        verifyUrlMapping("/project/999/testGroups", controller: 'testGroup', action: 'testGroups') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/testGroup/create", controller: 'testGroup', action: 'create') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/testGroup/show/123", controller: 'testGroup', action: 'show') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/999/testGroup/edit/123", controller: 'testGroup', action: 'edit') {
            id = 123
            projectId = 999
        }
    }

    void "verify test cycle forward and reverse mappings"() {
        mockController(TestCycleController)

        expect:
        verifyUrlMapping("/testCycle/addTests", controller: 'testCycle', action: 'addTests')
        verifyUrlMapping("/project/999/testCycle/show/123", controller: 'testCycle', action: 'show') {
            id = 123
            projectId = 999
        }
    }

    void "verify test iteration forward and reverse mappings"() {
        mockController(TestIterationController)

        expect:
        verifyUrlMapping("/testIteration/update", controller: 'testIteration', action: 'update')
        verifyUrlMapping("/project/999/testIteration/show/123", controller: 'testIteration', action: 'show') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/999/testIteration/execute/123", controller: 'testIteration', action: 'execute') {
            id = 123
            projectId = 999
        }
    }
}
