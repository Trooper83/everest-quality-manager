package com.everlution.test.service

import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Scenario
import com.everlution.ScenarioService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class ScenarioServiceSpec extends Specification {

    PersonService personService
    ProjectService projectService
    ScenarioService scenarioService
    SessionFactory sessionFactory

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    private Long setupData() {
        Project project = new Project(name: "ScenarioServiceSpec Project", code: "TTT").save()
        Scenario scenario = new Scenario(person: person, name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project).save()
        new Scenario(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: project).save()
        new Scenario(person: person, name: "third", description: "desc3",
                executionMethod: "Automated", type: "API", project: project).save()
        new Scenario(person: person, name: "fourth", description: "desc4",
                executionMethod: "Manual", type: "UI", project: project).save()
        scenario.id
    }

    void "test get"() {
        def id = setupData()

        expect:
        scenarioService.get(id) != null
    }

    void "get returns null for not found id"() {
        expect:
        scenarioService.get(9999999999) == null
    }

    void "test list with no args"() {
        setupData()

        when:
        List<Scenario> scenarioList = scenarioService.list()

        then:
        scenarioList.size() > 0
    }

    void "test list with max args"() {
        setupData()

        when:
        List<Scenario> scenarioList = scenarioService.list(max: 1)

        then:
        scenarioList.size() == 1
    }

    void "test list with offset args"() {
        setupData()

        when:
        List<Scenario> scenarioList = scenarioService.list(offset: 1)

        then:
        scenarioList.size() > 0
    }

    void "test count"() {
        setupData()

        expect:
        scenarioService.count() > 1
    }

    void "test delete"() {
        Long scenarioId = setupData()

        given:
        def count = scenarioService.count()

        when:
        scenarioService.delete(scenarioId)
        sessionFactory.currentSession.flush()

        then:
        scenarioService.count() == count - 1
    }

    void "test save"() {
        when:
        Project project = new Project(name: "Test Case Save Project", code: "TCS").save()
        Scenario scenario = new Scenario(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project)
        scenarioService.save(scenario)

        then:
        scenario.id != null
    }

    void "save throws validation exception for failed validation"() {
        when:
        Scenario scenario = new Scenario(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API")
        scenarioService.save(scenario)

        then:
        thrown(ValidationException)
    }

    void "read returns instance"() {
        setup:
        def id = setupData()

        expect:
        scenarioService.read(id) instanceof Scenario
    }

    void "read returns null for not found id"() {
        expect:
        scenarioService.read(999999999) == null
    }

    void "find all by project returns only scenarios with project"() {
        given:
        setupData()
        def person = new Person(email: "test98899@test.com", password: "password").save()
        def project = projectService.list(max: 1).first()
        new Scenario(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        when:
        def scenarios = scenarioService.findAllByProject(project)

        then:
        scenarios.size() > 0
        scenarios.every { it.project.id == project.id }
    }

    void "find all by project with null project returns empty list"() {
        given:
        setupData()

        when:
        def scenarios = scenarioService.findAllByProject(null)

        then:
        scenarios.size() == 0
        noExceptionThrown()
    }
}
