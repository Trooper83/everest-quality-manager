package com.manager.quality.everest.test.unit.testresult

import com.manager.quality.everest.domains.AutomatedTest
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.TestResult
import com.manager.quality.everest.domains.TestRun
import grails.test.hibernate.HibernateSpec
import org.springframework.dao.InvalidDataAccessApiUsageException
import spock.lang.Shared

class TestResultHibernateSpec extends HibernateSpec {

    List<Class> getDomainClasses() { [Project, Person, TestResult] }

    @Shared Person person
    @Shared Project project

    def setup() {
        project = new Project(name: "tc domain project321", code: "td5").save()
        person = new Person(email: "test@test.com", password: "!Testing@t123").save()
    }

    void "dateCreated populated on save"() {
        when:
        def at = new AutomatedTest(fullName: 'fullname', project: project).save()
        def tr = new TestRun(name: "test run", project: project).save()
        TestResult r = new TestResult(automatedTest: at, result: 'PASSED', testRun: tr).save()

        then:
        r.dateCreated != null
    }

    void "save does not cascade to automatedTest"() {
        given:
        def at = new AutomatedTest(fullName: "fullname", project: project)
        def t = new TestRun(name: "test run", project: project).save()
        def tr = new TestResult(automatedTest: at, result: "SKIPPED", testRun: t)

        expect:
        at.id == null

        when:
        tr.save()

        then:
        thrown(InvalidDataAccessApiUsageException)
    }

    void "delete does not cascade to automatedTest"() {
        given:
        def at = new AutomatedTest(fullName: "fullname", project: project).save()
        def t = new TestRun(name: "test run", project: project).save()
        def tr = new TestResult(automatedTest: at, result: "SKIPPED", testRun: t).save()

        when:
        tr.delete()

        then:
        AutomatedTest.get(at.id) != null
    }
}
