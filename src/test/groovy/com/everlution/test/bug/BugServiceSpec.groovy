package com.everlution.test.bug

import com.everlution.Bug
import com.everlution.BugService
import com.everlution.Person
import com.everlution.Project
import com.everlution.command.RemovedItems
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification

class BugServiceSpec extends Specification implements ServiceUnitTest<BugService>, DataTest {

    @Shared Person person
    @Shared Project project

    def setupSpec() {
        mockDomain(Bug)
    }

    def setup() {
        project = new Project(name: "BugServiceSpec Project", code: "BP3").save()
        person = new Person(email: "test123@test.com", password: "password").save()
        new Bug(person: person, description: "Found a bug", name: "Name of the bug", project: project).save()
        new Bug(person: person, description: "Found a bug again!", name: "Name of the bug again", project: project).save(flush: true)
    }

    void "get with valid id returns instance"() {
        expect: "valid instance"
        service.get(1) instanceof Bug
    }

    void "read returns instance"() {
        expect: "valid instance"
        service.read(1) instanceof Bug
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }

    void "list max args param returns correct value"() {
        expect:
        service.list(max: 1).size() == 1
        service.list(max: 2).size() == 2
        service.list().size() == 2
        service.list(offset: 1).size() == 1
    }

    void "count returns number of bugs"() {
        expect:
        service.count() == 2
    }

    void "delete with valid id deletes instance"() {
        given:
        def proj = new Project(name: "BugServ", code: "BPZ").save()
        def per = new Person(email: "test321@test.com", password: "password").save()
        def bug = new Bug(person: per, description: "Found a bug again!",
                name: "Name of the bug again", project: proj).save(flush: true)

        expect:
        service.get(bug.id) != null

        when:
        service.delete(bug.id)
        currentSession.flush()

        then:
        service.get(bug.id) == null
    }

    void "save with valid bug returns instance"() {
        given:
        def bug = new Bug(person: person, description: "Found a bug again!",
                name: "Name of the bug", project: project).save()

        when:
        def saved = service.save(bug)

        then:
        saved instanceof Bug
    }

    void "save with invalid bug throws validation exception"() {
        given:
        def bug = new Bug(name: "name")

        when:
        service.save(bug)

        then:
        thrown(ValidationException)
    }

    void "saveUpdate with invalid bug throws validation exception"() {
        given:
        def bug = new Bug(name: "name")

        when:
        service.saveUpdate(bug, new RemovedItems())

        then:
        thrown(ValidationException)
    }

    void "saveUpdate returns valid instance"() {
        given:
        def bug = new Bug(person: person, description: "Found a bug again!",
                name: "Name of the bug", project: project).save()

        when:
        def saved = service.saveUpdate(bug, new RemovedItems())

        then:
        saved instanceof Bug
    }
}
