package com.everlution.test.service

import com.everlution.Bug
import com.everlution.BugService
import com.everlution.Person
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.TestStepService
import com.everlution.command.RemovedItems
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.validation.ValidationException
import org.hibernate.SessionFactory
import spock.lang.Specification

@Rollback
@Integration
class BugServiceSpec extends Specification {

    BugService bugService
    ProjectService projectService
    SessionFactory sessionFactory
    TestStepService testStepService

    private Long setupData() {
        Project project = new Project(name: "BugServiceSpec Project", code: "BP3").save()
        def person = new Person(email: "test123@test.com", password: "password").save()
        Bug bug = new Bug(person: person, description: "Found a bug", name: "Name of the bug", project: project).save()
        new Bug(person: person, description: "Found a bug again!", name: "Name of the bug again", project: project).save()
        bug.id
    }

    void "test get"() {
        setupData()

        expect:
        bugService.get(1) != null
    }

    void "get returns null for not found id"() {
        expect:
        bugService.get(9999999999) == null
    }

    void "test list with no args"() {
        setupData()

        when:
        List<Bug> bugList = bugService.list()

        then:
        bugList.size() > 0
    }

    void "test list with max args"() {
        setupData()

        when:
        List<Bug> bugList = bugService.list(max: 1)

        then:
        bugList.size() == 1
    }

    void "test list with offset args"() {
        setupData()

        when:
        List<Bug> bugList = bugService.list(offset: 1)

        then:
        bugList.size() > 0
    }

    void "test count"() {
        setupData()

        expect:
        bugService.count() > 0
    }

    void "test delete"() {
        Long bugId = setupData()

        given:
        def c = bugService.count()

        when:
        bugService.delete(bugId)
        sessionFactory.currentSession.flush()

        then:
        bugService.count() == c - 1
    }

    void "test save"() {
        when:
        def person = new Person(email: "test988@test.com", password: "password").save()
        Project project = new Project(name: "BugServiceSpec Project", code: "BPM").save()
        Bug bug = new Bug(person: person, description: "Found a bug123", name: "Name of the bug123", project: project)
        bugService.save(bug)

        then:
        bug.id != null
    }

    void "save throws exception with validation fail"() {
        when:
        def person = new Person(email: "test@test.com", password: "password").save()
        Bug bug = new Bug(person: person, description: "Found a bug123", name: "Name of the bug123")
        bugService.save(bug)

        then:
        thrown(ValidationException)
    }

    void "saveUpdate throws exception with validation fail"() {
        when:
        def person = new Person(email: "test@test.com", password: "password").save()
        Bug bug = new Bug(person: person, description: "Found a bug123", name: "Name of the bug123")
        bugService.saveUpdate(bug, new RemovedItems())

        then:
        thrown(ValidationException)
    }

    void "saveUpdate removes steps"() {
        given: "valid test case with step"
        def project = projectService.list(max: 1).first()
        def person = new Person(email: "test999@test.com", password: "password").save()
        def step = new Step(action: "action", result: "result")
        def bug = new Bug(person: person, name: "second", description: "desc2",
                project: project, steps: [step]).save(failOnError: true)

        expect:
        testStepService.get(step.id) != null
        bug.steps.size() == 1

        when: "call saveUpdate"
        def removed = new RemovedItems()
        removed.stepIds = [step.id]
        bugService.saveUpdate(bug, removed)

        then: "step is removed"
        bug.steps.size() == 0
        bugService.get(bug.id).steps.size() == 0
    }

    void "read returns instance"() {
        setup:
        def id = setupData()

        expect:
        bugService.read(id) instanceof Bug
    }

    void "read returns null for not found id"() {
        expect:
        bugService.read(999999999) == null
    }

    void "find all bugs by project returns only bugs with project"() {
        given:
        setupData()
        def project = projectService.list(max: 1).first()

        when:
        def bugs = bugService.findAllByProject(project)

        then:
        bugs.size() > 0
        bugs.every { it.project.id == project.id }
    }

    void "find all bugs by project with null project returns empty list"() {
        given:
        setupData()

        when:
        def bugs = bugService.findAllByProject(null)

        then:
        bugs.size() == 0
        noExceptionThrown()
    }
}
