package com.everlution.test.service

import com.everlution.AutomatedTest
import com.everlution.AutomatedTestService
import com.everlution.ProjectService
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
}
