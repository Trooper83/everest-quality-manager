package com.everlution.test.testrun

import com.everlution.AutomatedTest
import com.everlution.Project
import com.everlution.TestResult
import com.everlution.TestRun
import grails.test.hibernate.HibernateSpec
import org.springframework.dao.InvalidDataAccessApiUsageException

class TestRunHibernateSpec extends HibernateSpec {

    List<Class> getDomainClasses() { [Project, TestRun] }

    void "dateCreated populated on save"() {
        when:
        def p = new Project(name: "test project", code: "tpp").save()
        def t = new TestRun(name: "name", project: p).save()

        then:
        t.dateCreated != null
    }

    void "save does not cascade to project"() {
        given:
        def p = new Project(name: 'name', code: 'cod')

        when:
        new TestRun(name: "name", project: p).save()

        then:
        thrown(InvalidDataAccessApiUsageException)
    }

    void "delete does not cascade to project"() {
        given:
        def p = new Project(name: 'name', code: 'cod').save()
        def t = new TestRun(name: "name", project: p).save()

        expect:
        TestRun.get(t.id) != null

        when:
        t.delete()

        then:
        TestRun.get(t.id) == null
        Project.get(p.id) != null
    }

    void "save cascades to testResult"() {
        given:
        def p = new Project(name: "name", code: "ooo").save()
        def at = new AutomatedTest(fullName: "full name", project: p).save()
        def tr = new TestResult(automatedTest: at, result: "Failed")
        def run = new TestRun(name: "name", project: p).addToTestResults(tr)

        when:
        run.save()

        then:
        tr.id != null
    }

    void "delete cascades to testResult"() {
        given:
        def p = new Project(name: "name", code: "ooo").save()
        def at = new AutomatedTest(fullName: "full name", project: p).save()
        def tr = new TestResult(automatedTest: at, result: "Failed")
        def run = new TestRun(name: "name", project: p).addToTestResults(tr)
        run.save()

        expect:
        TestResult.get(tr.id) != null

        when:
        run.delete()

        then:
        TestResult.get(tr.id) == null
    }
}
