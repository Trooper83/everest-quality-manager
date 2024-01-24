package com.everlution.test.testresult

import com.everlution.AutomatedTest
import com.everlution.Person
import com.everlution.Project
import com.everlution.TestResult
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
        TestResult r = new TestResult(automatedTest: at, result: 'Passed').save()

        then:
        r.dateCreated != null
    }

    void "save does not cascade to automatedTest"() {
        given:
        def at = new AutomatedTest(fullName: "fullname", project: project)
        def tr = new TestResult(automatedTest: at, result: "Skipped")

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
        def tr = new TestResult(automatedTest: at, result: "Skipped").save()

        when:
        tr.delete()

        then:
        AutomatedTest.get(at.id) != null
    }
}
