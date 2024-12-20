package com.manager.quality.everest.test.service

import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.Scenario
import com.manager.quality.everest.services.scenario.ScenarioService
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
    @Shared Project project

    def setup() {
        person = personService.list(max: 1).first()
    }

    private Long setupData() {
        project = new Project(name: "ScenarioServiceSpec Project", code: "TTT").save()
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

    void "test delete"() {
        setup:
        Long scenarioId = setupData()

        expect:
        Scenario.findById(scenarioId) != null

        when:
        scenarioService.delete(scenarioId)
        sessionFactory.currentSession.flush()

        then:
        Scenario.findById(scenarioId) == null
    }

    void "test save"() {
        when:
        Project proj = new Project(name: "Test Case Save Project", code: "TCS").save()
        Scenario scenario = new Scenario(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: proj)
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
        def proj = projectService.list(max: 1).first()
        def scn = new Scenario(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: proj).save()

        when:
        def scenarios = scenarioService.findAllByProject(project, [:]).results

        then:
        scenarios.size() > 0
        scenarios.every { it.project.id == project.id }
        !scenarios.contains(scn)
    }

    void "find all by project with null project returns empty list"() {
        given:
        setupData()

        when:
        def scenarios = scenarioService.findAllByProject(null, [:])

        then:
        scenarios.results.size() == 0
        noExceptionThrown()
    }

    void "find all in project by name returns scenarios"() {
        setup:
        setupData()

        expect:
        def scenarios = scenarioService.findAllInProjectByName(project, "first", [:])
        scenarios.results.first().name == "first"
    }
}
