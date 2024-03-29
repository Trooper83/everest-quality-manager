package com.everlution.test.service

import com.everlution.domains.AutomatedTest
import com.everlution.services.automatedtest.AutomatedTestService
import com.everlution.services.project.ProjectService
import com.everlution.test.support.DataFactory
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.validation.ValidationException
import spock.lang.Specification

@Integration
@Rollback
class AutomatedTestServiceSpec extends Specification {

    AutomatedTestService automatedTestService
    ProjectService projectService

    void "findOrSave creates automated test when not found"() {
        when:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.findOrSave(p, "full name")

        then:
        a != null
    }

    void "findOrSave returns same automated test when found"() {
        when:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.findOrSave(p, "full name")
        def at = automatedTestService.findOrSave(p, "full name")

        then:
        a == at
    }

    void "findOrSave throws validation exception"() {
        when:
        def p = projectService.list(max:1).first()
        automatedTestService.findOrSave(p, "")

        then:
        thrown(ValidationException)
    }

    void "save returns instance"() {
        when:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(project: p, fullName: "fullname"))

        then:
        a != null
    }

    void "save throws validation exception"() {
        when:
        def p = projectService.list(max:1).first()
        automatedTestService.save(new AutomatedTest(project: p, fullName: ""))

        then:
        thrown(ValidationException)
    }

    void "save throws validation exception when fullName not unique in project"() {
        when:
        def p = projectService.list(max:1).first()
        automatedTestService.save(new AutomatedTest(project: p, fullName: "testing fullname uniqueness"))
        automatedTestService.save(new AutomatedTest(project: p, fullName: "testing fullname uniqueness"))

        then:
        thrown(ValidationException)
    }

    void "findByProjectAndFullName returns automated test"() {
        given:
        def p = projectService.list(max:1).first()
        def a = new AutomatedTest(project: p, fullName: "find me")
        automatedTestService.save(a)

        when:
        def found = automatedTestService.findByProjectAndFullName(p, "find me")

        then:
        found != null
        found == a
    }

    void "findAllByProject returns tests"() {
        given:
        def p = projectService.list(max:1).first()
        def a = new AutomatedTest(project: p, fullName: "find me")
        automatedTestService.save(a)

        when:
        def tests = automatedTestService.findAllByProject(p, [:]).results

        then:
        tests.contains(a)
    }

    void "findAllByProject returns empty list when project null"() {
        when:
        def t = automatedTestService.findAllByProject(null, [:])

        then:
        t.results.empty
        noExceptionThrown()
    }

    void "findAllInProjectByFullName returns tests"() {
        given:
        def p = projectService.list(max:1).first()
        def a = new AutomatedTest(project: p, fullName: "find me")
        automatedTestService.save(a)

        when:
        def tests = automatedTestService.findAllInProjectByFullName(p, "find", [:])

        then:
        tests.results.contains(a)
    }

    void "findAllInProjectByFullName returns empty list when project null"() {
        when:
        def t = automatedTestService.findAllInProjectByFullName(null, "", [:])

        then:
        t.results.empty
        noExceptionThrown()
    }

    void "get returns AutomatedTest"() {
        given:
        def p = projectService.list(max:1).first()
        def a = new AutomatedTest(project: p, fullName: "find me")
        automatedTestService.save(a)

        when:
        def test = automatedTestService.get(a.id)

        then:
        test != null
    }

    void "get returns null when not found"() {
        when:
        def t = automatedTestService.get(999999999)

        then:
        t == null
        noExceptionThrown()
    }

    void "countByProject returns number of tests in project"() {
        given:
        def p = DataFactory.createProject()
        def a = new AutomatedTest(project: p, fullName: "count me")
        def a1 = new AutomatedTest(project: p, fullName: "count me 123")
        automatedTestService.save(a)
        automatedTestService.save(a1)

        when:
        def c = automatedTestService.countByProject(p)

        then:
        c == 2
    }
}
