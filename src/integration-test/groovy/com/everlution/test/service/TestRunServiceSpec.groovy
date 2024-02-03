package com.everlution.test.service

import com.everlution.ProjectService
import com.everlution.TestRun
import com.everlution.TestRunService
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Rollback
@Integration
class TestRunServiceSpec extends Specification {

    ProjectService projectService
    TestRunService testRunService

    void "save returns instance"() {
        given:
        def p = projectService.list(max:1).first()
        def t = new TestRun(name: "name of the run", project: p)

        when:
        def tr = testRunService.save(t)

        then:
        tr.id != null
    }

    void "get returns instance"() {
        given:
        def p = projectService.list(max:1).first()
        def t = new TestRun(name: "name of the run to find", project: p)
        def tr = testRunService.save(t)

        when:
        def found = testRunService.get(tr.id)

        then:
        found != null
    }
}
