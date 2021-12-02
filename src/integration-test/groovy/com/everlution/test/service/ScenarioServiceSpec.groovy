package com.everlution.test.service

import com.everlution.Project
import com.everlution.Scenario
import com.everlution.ScenarioService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class ScenarioServiceSpec extends Specification {

    ScenarioService scenarioService
    SessionFactory sessionFactory

    private Long setupData() {
        Project project = new Project(name: "ScenarioServiceSpec Project", code: "TTT").save()
        Scenario scenario = new Scenario(creator: "test",name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project).save()
        new Scenario(creator: "test",name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: project).save()
        new Scenario(creator: "test",name: "third", description: "desc3",
                executionMethod: "Automated", type: "API", project: project).save()
        new Scenario(creator: "test",name: "fourth", description: "desc4",
                executionMethod: "Manual", type: "UI", project: project).save()
        scenario.id
    }

    void "test get"() {
        def id = setupData()

        expect:
        scenarioService.get(id) != null
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
        Scenario scenario = new Scenario(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project)
        scenarioService.save(scenario)

        then:
        scenario.id != null
    }

    void "test delete all by project"() {
        Long id = setupData()

        given:
        def project = scenarioService.get(id).project

        expect:
        Scenario.findAllByProject(project).size() == 4

        when:
        scenarioService.deleteAllScenariosByProject(project)
        sessionFactory.currentSession.flush()

        then:
        Scenario.findAllByProject(project).size() == 0
    }
}
