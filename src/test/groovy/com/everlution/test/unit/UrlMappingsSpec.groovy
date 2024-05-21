package com.everlution.test.unit

import com.everlution.controllers.AutomatedTestController
import com.everlution.controllers.BugController
import com.everlution.controllers.ProjectController
import com.everlution.controllers.ReleasePlanController
import com.everlution.controllers.ScenarioController
import com.everlution.controllers.StepTemplateController
import com.everlution.controllers.TestCaseController
import com.everlution.controllers.TestCycleController
import com.everlution.controllers.TestGroupController
import com.everlution.controllers.TestIterationController
import com.everlution.controllers.TestRunController
import com.everlution.controllers.UrlMappings
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
        verifyUrlMapping("/project/999/testCases", controller: 'testCase', action: 'testCases') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/testCase/create", controller: 'testCase', action: 'create') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/testCase/save", controller: 'testCase', action: 'save') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/testCase/update/123", controller: 'testCase', action: 'update') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/999/testCase/delete/123", controller: 'testCase', action: 'delete') {
            id = 123
            projectId = 999
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
        verifyUrlMapping("/project/home/123", controller: 'project', action: 'home') {
            projectId = 123
        }
        verifyUrlMapping("/project/show/123", controller: 'project', action: 'show') {
            projectId = 123
        }
        verifyUrlMapping("/project/create", controller: 'project', action: 'create')
        verifyUrlMapping("/project/save", controller: 'project', action: 'save')
        verifyUrlMapping("/project/update", controller: 'project', action: 'update')
        verifyUrlMapping("/project/edit/123", controller: 'project', action: 'edit') {
            projectId = 123
        }
        verifyUrlMapping("/project/delete/123", controller: 'project', action: 'delete') {
            id = 123
        }
    }

    void "verify bug forward and reverse mappings"() {
        mockController(BugController)

        expect:
        verifyUrlMapping("/project/123/bugs", controller: 'bug', action: 'bugs') {
            projectId = 123
        }
        verifyUrlMapping("/project/123/bug/create", controller: 'bug', action: 'create') {
            projectId = 123
        }
        verifyUrlMapping("/project/999/bug/save", controller: 'bug', action: 'save') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/bug/update/123", controller: 'bug', action: 'update') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/999/bug/delete/123", controller: 'bug', action: 'delete') {
            id = 123
            projectId = 999
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
        verifyUrlMapping("/project/123/scenarios", controller: 'scenario', action: 'scenarios') {
            projectId = 123
        }
        verifyUrlMapping("/project/123/scenario/create", controller: 'scenario', action: 'create') {
            projectId = 123
        }
        verifyUrlMapping("/project/999/scenario/save", controller: 'scenario', action: 'save') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/scenario/update/123", controller: 'scenario', action: 'update') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/999/scenario/delete/123", controller: 'scenario', action: 'delete') {
            id = 123
            projectId = 999
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
        verifyUrlMapping("/project/999/releasePlans", controller: 'releasePlan', action: 'releasePlans') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/releasePlan/create", controller: 'releasePlan', action: 'create') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/releasePlan/save", controller: 'releasePlan', action: 'save') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/releasePlan/update/123", controller: 'releasePlan', action: 'update') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/999/releasePlan/delete/123", controller: 'releasePlan', action: 'delete') {
            id = 123
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
        verifyUrlMapping("/project/123/testGroups", controller: 'testGroup', action: 'testGroups') {
            projectId = 123
        }
        verifyUrlMapping("/project/123/testGroup/create", controller: 'testGroup', action: 'create') {
            projectId = 123
        }
        verifyUrlMapping("/project/999/testGroup/save", controller: 'testGroup', action: 'save') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/testGroup/update/123", controller: 'testGroup', action: 'update') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/999/testGroup/delete/123", controller: 'testGroup', action: 'delete') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/123/testGroup/show/123", controller: 'testGroup', action: 'show') {
            id = 123
            projectId = 123
        }
        verifyUrlMapping("/project/123/testGroup/edit/123", controller: 'testGroup', action: 'edit') {
            id = 123
            projectId = 123
        }
    }

    void "verify test cycle forward and reverse mappings"() {
        mockController(TestCycleController)

        expect:
        verifyUrlMapping("/project/999/testCycle/addTests", controller: 'testCycle', action: 'addTests') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/testCycle/show/123", controller: 'testCycle', action: 'show') {
            id = 123
            projectId = 999
        }
    }

    void "verify test iteration forward and reverse mappings"() {
        mockController(TestIterationController)

        expect:
        verifyUrlMapping("/project/999/testIteration/update/123", controller: 'testIteration', action: 'update') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/999/testIteration/show/123", controller: 'testIteration', action: 'show') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/999/testIteration/execute/123", controller: 'testIteration', action: 'execute') {
            id = 123
            projectId = 999
        }
    }

    void "verify stepTemplate forward and reverse mappings"() {
        mockController(StepTemplateController)

        expect:
        verifyUrlMapping("/project/123/stepTemplates", controller: 'stepTemplate', action: 'stepTemplates') {
            projectId = 123
        }
        verifyUrlMapping("/project/123/stepTemplate/create", controller: 'stepTemplate', action: 'create') {
            projectId = 123
        }
        verifyUrlMapping("/project/999/stepTemplate/save", controller: 'stepTemplate', action: 'save') {
            projectId = 999
        }
        verifyUrlMapping("/project/999/stepTemplate/update/123", controller: 'stepTemplate', action: 'update') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/999/stepTemplate/delete/123", controller: 'stepTemplate', action: 'delete') {
            id = 123
            projectId = 999
        }
        verifyUrlMapping("/project/123/stepTemplate/show/123", controller: 'stepTemplate', action: 'show') {
            id = 123
            projectId = 123
        }
        verifyUrlMapping("/project/123/stepTemplate/edit/123", controller: 'stepTemplate', action: 'edit') {
            id = 123
            projectId = 123
        }
        verifyUrlMapping("/project/123/stepTemplate/search", controller: 'stepTemplate', action: 'search') {
            projectId = 123
        }
        verifyUrlMapping("/project/123/stepTemplate/getRelatedTemplates", controller: 'stepTemplate', action: 'getRelatedTemplates') {
            projectId = 123
        }
    }

    void "verify testRun forward and reverse mappings"() {
        mockController(TestRunController)

        expect:
        verifyUrlMapping("/api/testRuns", controller: 'testRun', action: 'save')
    }

    void "verify automatedTest forward and reverse mappings"() {
        mockController(AutomatedTestController)

        expect:
        verifyUrlMapping("/project/123/automatedTests", controller: 'automatedTest', action: 'automatedTests') {
            projectId = 123
        }
        verifyUrlMapping("/project/123/automatedTest/show/321", controller: 'automatedTest', action: 'show') {
            projectId = 123
            id = 321
        }
    }

    void "verify testRun forward and reverse mappings"() {
        mockController(TestRunController)

        expect:
        verifyUrlMapping("/api/testRuns", controller: 'testRun', action: 'save')

        verifyUrlMapping("/project/123/testRuns", controller: 'testRun', action: 'testRuns') {
            projectId = 123
        }
        verifyUrlMapping("/project/123/testRun/show/321", controller: 'testRun', action: 'show') {
            projectId = 123
            id = 321
        }
    }

    void "verify user forward and reverse mappings"() {
        expect:
        false
    }
}
