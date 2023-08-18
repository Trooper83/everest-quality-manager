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
        new Bug(person: person, description: "Found a bug", name: "first bug", project: project, status: "Open",
                actual: "actual", expected: "expected").save()
        new Bug(person: person, description: "Found a bug again!", name: "Second bug", project: project, status: "Open",
                actual: "actual", expected: "expected").save(flush: true)
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

    void "delete with valid id deletes instance"() {
        given:
        def proj = new Project(name: "BugServ", code: "BPZ").save()
        def per = new Person(email: "test321@test.com", password: "password").save()
        def bug = new Bug(person: per, description: "Found a bug again!",
                name: "Name of the bug again", project: proj, status: "Open",
                actual: "actual", expected: "expected").save(flush: true)

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
                name: "Name of the bug", project: project, status: "Open",
                actual: "actual", expected: "expected").save()

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
                name: "Name of the bug", project: project, status: "Open",
                actual: "actual", expected: "expected").save()

        when:
        def saved = service.saveUpdate(bug, new RemovedItems())

        then:
        saved instanceof Bug
    }

    void "find all bugs by project returns bugs"() {
        when:
        def bugs = service.findAllByProject(project, [:])

        then:
        bugs.results instanceof List<Bug>
    }

    void "find all bugs by project only returns bugs with project"() {
        given:
        def proj = new Project(name: "BugServiceSpec Project1223", code: "BP8").save()
        def bug = new Bug(person: person, description: "Found a bug", name: "Name of the bug", project: proj,
                status: "Open", actual: "actual", expected: "expected").save(flush: true)

        expect:
        Bug.list().contains(bug)

        when:
        def bugs = service.findAllByProject(project, [:])

        then:
        bugs.results.every { it.project.id == project.id }
        bugs.results.size() > 0
        bugs.results.size() == bugs.count
        !bugs.results.contains(bug)
    }

    void "find all bugs by project with null project id returns empty list"() {
        when:
        def bugs = service.findAllByProject(null, [:])

        then:
        noExceptionThrown()
        bugs.results.size() == 0
    }

    void "find all by name ilike returns bugs"(String q) {
        expect:
        def bugs = service.findAllInProjectByName(project, q, [:])
        bugs.results.first().name == "first bug"

        where:
        q << ['first', 'fi', 'irs', 't bu', 'FIRST']
    }

    void "find all in project by name with null project"() {
        when:
        def bugs = service.findAllInProjectByName(null, 'bug', [:])

        then:
        bugs.results.empty
        bugs.count == 0
        noExceptionThrown()
    }

    void "find all by name ilike with string"(String s, int size) {
        expect:
        def bugs = service.findAllInProjectByName(project, s, [:])
        bugs.results.size() == size
        bugs.count == size

        where:
        s           | size
        null        | 0
        ''          | 2
        'not found' | 0
        'bug'       | 2
    }

    void "find all by name iLike by project only returns bugs with project"() {
        given:
        def proj = new Project(name: "BugServiceSpec Project1", code: "BP4").save()
        def bug = new Bug(person: person, description: "Found a bug", name: "Name of the bug", project: proj,
                status: "Open", actual: "actual", expected: "expected").save(flush: true)

        expect:
        Bug.list().contains(bug)

        when:
        def bugs = service.findAllInProjectByName(project, 'bug', [:])

        then:
        bugs.results.every { it.project.id == project.id }
        bugs.results.size() > 0
        !bugs.results.contains(bug)
    }
}
