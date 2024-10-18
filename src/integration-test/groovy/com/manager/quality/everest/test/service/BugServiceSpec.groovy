package com.manager.quality.everest.test.service

import com.manager.quality.everest.domains.Bug
import com.manager.quality.everest.services.bug.BugService
import com.manager.quality.everest.services.step.StepService
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.Step
import com.manager.quality.everest.controllers.command.RemovedItems
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.validation.ValidationException
import org.hibernate.SessionFactory
import spock.lang.Shared
import spock.lang.Specification

@Rollback
@Integration
class BugServiceSpec extends Specification {

    BugService bugService
    SessionFactory sessionFactory
    StepService stepService

    @Shared Project project

    private Long setupData() {
        project = new Project(name: "BugServiceSpec Project", code: "BP3").save()
        def person = new Person(email: "test123@test.com", password: "!Password2022").save()
        Bug bug = new Bug(person: person, description: "Found a bug", name: "Name of the bug", project: project,
                status: "Open", actual: "actual", expected: "expected").save()
        new Bug(person: person, description: "Found a bug again!", name: "Name of the bug again", project: project,
                status: "Open", actual: "actual", expected: "expected").save()
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

    void "test delete"() {
        given:
        Long bugId = setupData()

        expect:
        Bug.findById(bugId) != null

        when:
        bugService.delete(bugId)
        sessionFactory.currentSession.flush()

        then:
        Bug.findById(bugId) == null
    }

    void "test save"() {
        when:
        def person = new Person(email: "test988@test.com", password: "!Password2022").save()
        Project proj = new Project(name: "BugServiceSpec Project", code: "BPM").save()
        Bug bug = new Bug(person: person, description: "Found a bug123", name: "Name of the bug123", project: proj,
                status: "Open", actual: "actual", expected: "expected")
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

    void "saveUpdate removes free form steps"() {
        setupData()
        given: "valid test case with step"
        def person = new Person(email: "test999@test.com", password: "!Password2022").save()
        def step = new Step(act: "action", result: "result").save()
        def bug = new Bug(person: person, name: "second", description: "desc2", project: project,
                steps: [step], status: "Open", actual: "actual", expected: "expected").save(failOnError: true)

        expect:
        stepService.get(step.id) != null
        bug.steps.size() == 1

        when: "call saveUpdate"
        def removed = new RemovedItems()
        removed.stepIds = [step.id]
        bugService.saveUpdate(bug, removed)

        then: "step is removed"
        bug.steps.size() == 0
        bugService.get(bug.id).steps.size() == 0
    }

    void "saveUpdate removes builder steps"() {
        setupData()
        given: "valid test case with step"
        def person = new Person(email: "test999@test.com", password: "!Password2022").save()
        def step = new Step(act: "action", result: "result", isBuilderStep: true).save()
        def bug = new Bug(person: person, name: "second", description: "desc2", project: project,
                steps: [step], status: "Open", actual: "actual", expected: "expected").save(failOnError: true)

        expect:
        stepService.get(step.id) != null
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

        when:
        def bugs = bugService.findAllByProject(project, [:])

        then:
        bugs.results.size() > 0
        bugs.results.every { it.project.id == project.id }
    }

    void "find all bugs by project with null project returns empty list"() {
        given:
        setupData()

        when:
        def bugs = bugService.findAllByProject(null, [:]).results

        then:
        bugs.size() == 0
        noExceptionThrown()
    }

    void "find all in project by name returns bugs"() {
        setup:
        def id = setupData()

        expect:
        def bug = bugService.findAllInProjectByName(project, "again", [:]).results
        bug.first().name == "Name of the bug again"
    }

    void "delete method deletes free form steps"() {
        setupData()
        given: "valid test case with step"
        def person = new Person(email: "test999@test.com", password: "!Password2022").save()
        def step = new Step(act: "action", result: "result").save()
        def bug = new Bug(person: person, name: "second", description: "desc2", project: project,
                steps: [step], status: "Open", actual: "actual", expected: "expected").save(failOnError: true)

        expect:
        stepService.get(step.id) != null
        bug.steps.size() == 1

        when: "call delete"
        bugService.delete(bug.id)

        then: "step is removed"
        stepService.get(step.id) == null
    }

    void "saveUpdate method deletes free form steps"() {
        setupData()
        given: "valid test case with step"
        def person = new Person(email: "test999@test.com", password: "!Password2022").save()
        def step = new Step(act: "action", result: "result").save()
        def bug = new Bug(person: person, name: "second", description: "desc2", project: project,
                steps: [step], status: "Open", actual: "actual", expected: "expected").save(failOnError: true)

        expect:
        stepService.get(step.id) != null

        when: "call saveUpdate"
        def removed = new RemovedItems()
        removed.stepIds = [step.id]
        bugService.saveUpdate(bug, removed)
        sessionFactory.currentSession.flush()

        then: "step is removed"
        stepService.get(step.id) == null
    }

    void "saveUpdate method does not delete free form steps when validation fails"() {
        setupData()
        given: "valid test case with step"
        def person = new Person(email: "test999@test.com", password: "!Password2022").save()
        def step = new Step(act: "action", result: "result").save()
        def bug = new Bug(person: person, name: "second", description: "desc2", project: project,
                steps: [step], status: "Open", actual: "actual", expected: "expected").save(failOnError: true)

        expect:
        stepService.get(step.id) != null

        when: "call saveUpdate"
        def removed = new RemovedItems()
        removed.stepIds = [step.id]
        bug.name = ''
        bugService.saveUpdate(bug, removed)

        then: "step is removed"
        thrown(ValidationException)
        stepService.get(step.id) != null
    }

    void "countByProjectAndStatus returns count of bugs"() {
        given:
        setupData()

        when:
        def c = bugService.countByProjectAndStatus(project, "Open")

        then:
        c == 2
    }
}
