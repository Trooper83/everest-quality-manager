package com.everlution.test.project

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.command.RemovedItems
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Specification

class ProjectServiceSpec extends Specification implements ServiceUnitTest<ProjectService>, DataTest {

    def setupSpec() {
        mockDomain(Project)
    }

    private void setupData() {
        new Project(name: "first project", code: "1st").save()
        new Project(name: "second project", code: "2nd").save()
        new Project(name: "third project", code: "3rd").save(flush: true)
    }

    void "get with valid id returns instance"() {
        setupData()

        expect: "valid instance"
        service.get(1) instanceof Project
    }

    void "read returns instance"() {
        setupData()

        expect: "valid instance"
        service.read(1) instanceof Project
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }

    void "list max args param returns correct value"() {
        setupData()

        expect:
        service.list(max: 1).size() == 1
        service.list(max: 2).size() == 2
        service.list().size() == 3
        service.list(offset: 1).size() == 2
    }

    void "count returns number of projects"() {
        setupData()

        expect:
        service.count() == 3
    }

    void "delete with valid id deletes instance"() {
        given:
        def p = new Project(name: "Unit Test Project For Service", code: "BPZ").save(flush: true)

        expect:
        service.get(p.id) != null

        when:
        service.delete(p.id)
        currentSession.flush()

        then:
        service.get(p.id) == null
    }

    void "save with valid project returns instance"() {
        given:
        def p = new Project(name: "Unit Test Project For Service", code: "BPZ")

        when:
        def saved = service.save(p)

        then:
        saved instanceof Project
    }

    void "save with invalid project throws validation exception"() {
        given:
        def p = new Project(name: "Unit Test Project For Service")

        when:
        service.save(p)

        then:
        thrown(ValidationException)
    }

    void "saveUpdate with invalid project throws validation exception"() {
        given:
        def p = new Project(name: "Unit Test Project For Service")

        when:
        service.saveUpdate(p, new RemovedItems())

        then:
        thrown(ValidationException)
    }

    void "saveUpdate returns valid instance"() {
        given:
        def p = new Project(name: "Unit Test Project For Service", code: "BPZ")

        when:
        def saved = service.saveUpdate(p, new RemovedItems())

        then:
        saved instanceof Project
    }

    void "find all by name ilike returns projects"(String q) {
        setup:
        setupData()

        expect:
        def projects = service.findAllByNameIlike(q)
        projects.first().name == "First project"

        where:
        q << ['first', 'fi', 'irs', 't pro', 'FIRST']
    }

    void "find all by name ilike with string"(String s, int size) {
        setup:
        setupData()

        expect:
        def projects = service.findAllByNameIlike(s)
        projects.size() == size

        where:
        s           | size
        null        | 0
        ''          | 3
        'not found' | 0
        'project'   | 3
    }
}
