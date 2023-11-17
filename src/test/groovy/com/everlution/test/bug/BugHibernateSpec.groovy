package com.everlution.test.bug

import com.everlution.Bug
import com.everlution.Person
import com.everlution.Project
import com.everlution.Step
import grails.test.hibernate.HibernateSpec
import org.springframework.dao.InvalidDataAccessApiUsageException
import spock.lang.Shared

class BugHibernateSpec extends HibernateSpec {

    @Shared Person person
    @Shared Project project

    def setup() {
        project = new Project(name: "bug domain project", code: "bdp").save()
        person = new Person(email: "test@test.com", password: "!Password2022").save()
    }

    void "dateCreated populates on save"() {
        given: "a bug instance"
        def bug = new Bug(person: person, name: "First Bug", description: "test", project: project, status: "Open",
                actual: "actual", expected: "expected").save()

        expect: "bug has date created"
        bug.dateCreated != null
    }

    void "lastUpdated populates on save"() {
        given: "a bug instance"
        def bug = new Bug(person: person, name: "First Bug", description: "test", project: project, status: "Open",
                actual: "actual", expected: "expected").save()

        when:
        bug.actual = "new actual"
        bug.save(flush: true)

        then: "bug has date created"
        bug.lastUpdated != null
    }

    void "test save does not cascade to project"() {
        when: "unsaved project is added to bug"
        Project proj = new Project(name: "BugServiceSpec Project2", code: "BMP")
        new Bug(person: person, description: "Found a bug123", name: "Name of the bug123", project: proj, status: "Open",
                actual: "actual", expected: "expected").save()

        then: "exception is thrown"
        thrown(InvalidDataAccessApiUsageException)
    }

    void "save does not cascade to person"() {
        when: "unsaved person"
        def p = new Person(email: "test@test.com", password: "password")
        new Bug(person: p, description: "Found a bug123", name: "Name of the bug123", project: project, status: "Open",
                actual: "actual", expected: "expected").save()

        then: "exception is thrown"
        thrown(InvalidDataAccessApiUsageException)
    }

    void "delete bug does not cascade to project"() {
        given: "valid project and bug"
        Bug bug = new Bug(name: "cascade project", description: "this should delete", person: person,
                project: project, status: "Open", actual: "actual", expected: "expected").save()

        expect:
        project.id != null
        bug.id != null

        when: "delete bug"
        bug.delete()
        sessionFactory.currentSession.flush()

        then: "project is not deleted"
        Project.findById(project.id) != null
    }

    void "test save bug with steps cascades"() {
        given: "unsaved bug and test steps"
        Step testStep = new Step(act: "do something", result: "something happened", person: person, project: project)
        Step testStep1 = new Step(act: "do something", result: "something happened", person: person, project: project)
        Bug bug = new Bug(person: person, name: "test", description: "desc",
               steps: [testStep, testStep1], project: project, status: "Open",
                actual: "actual", expected: "expected")

        expect:
        testStep.id == null
        testStep1.id == null

        when: "save bug"
        bug.save()

        then:
        testStep.id != null
        testStep1.id != null
    }

    void "test update case with steps"() {
        given: "valid bug instance"
        Step testStep = new Step(act: "do something", result: "something happened", person: person, project: project)
        Bug bug = new Bug(person: person, name: "test", description: "desc",
                steps: [testStep], project: project, status: "Open",
                actual: "actual", expected: "expected").save()

        when: "update steps"
        bug.steps[0].act = "edited action"
        bug.steps[0].result = "edited result"

        then: "updated steps are retrieved on the step instance"
        def step = Step.findById(testStep.id)
        step.act == "edited action"
        step.result == "edited result"
    }

    void "delete bug cascades to steps"() {
        given: "valid domain instances"
        Step testStep = new Step(act: "do something", result: "something happened")
        Bug bug = new Bug(person: person, name: "test", description: "desc",
                project: project, status: "Open",
                actual: "actual", expected: "expected").addToSteps(testStep)
        bug.save()

        expect:
        testStep.id != null
        bug.steps.size() == 1

        when: "delete bug"
        bug.delete()
        sessionFactory.currentSession.flush()

        then: "steps are not found"
        Step.findById(testStep.id) == null
    }

    void "test steps order persists"() {
        given: "save a bug"
        Step testStep = new Step(act: "do something", result: "something happened123", person: person, project: project)
        Step testStep1 = new Step(act: "I did something", result: "something happened231", person: person, project: project)
        Step testStep2 = new Step(act: "something happened", result: "something happened321", person: person, project: project)
        Bug bug = new Bug(person: person, name: "test", description: "desc",
                project: project, steps: [testStep, testStep1, testStep2], status: "Open",
                actual: "actual", expected: "expected")
        bug.save()

        when: "get the bug"
        def b = Bug.findById(bug.id)

        then: "step order is the same as when it was created"
        b.steps[0].id == testStep.id
        b.steps[1].id == testStep1.id
        b.steps[2].id == testStep2.id
    }
}
