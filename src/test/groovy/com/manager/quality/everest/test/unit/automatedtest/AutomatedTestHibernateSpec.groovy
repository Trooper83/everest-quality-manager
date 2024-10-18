package com.manager.quality.everest.test.unit.automatedtest

import com.manager.quality.everest.domains.AutomatedTest
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.domains.TestResult
import grails.test.hibernate.HibernateSpec
import org.springframework.dao.InvalidDataAccessApiUsageException
import spock.lang.Shared

class AutomatedTestHibernateSpec extends HibernateSpec {

    List<Class> getDomainClasses() { [Person, Project, TestResult, TestCase] }

    @Shared Project p

    def setup() {
        p = new Project(name: 'name', code: 'hbs').save()
    }

    void "dateCreated populated on save"() {
        when:
        def a = new AutomatedTest(fullName: 'full', project: p).save()

        then:
        a.dateCreated != null
    }

    void "fullName must be unique in project"() {
        given:
        def a = new AutomatedTest(project: p, fullName: 'full').save()

        when:
        def at = new AutomatedTest(project: p, fullName: 'full').save(flush: true)

        then:
        a != null
        at == null
    }

    void "fullName validates when same in separate project"() {
        given:
        def pr = new Project(code: 'cro', name: 'naming this').save()
        def a = new AutomatedTest(project: pr, fullName: 'full').save()

        when:
        def at = new AutomatedTest(project: p, fullName: 'full').save(flush: true)

        then:
        a != null
        at != null
    }

    void "save does not cascade to project"() {
        given:
        def pr = new Project(code: 'cro', name: 'naming this')
        def a = new AutomatedTest(project: pr, fullName: 'full')

        when:
        a.save()

        then:
        thrown(InvalidDataAccessApiUsageException)
    }

    void "delete does not cascade to project"() {
        given:
        def pr = new Project(code: 'cro', name: 'naming this').save()
        def a = new AutomatedTest(project: pr, fullName: 'full').save()

        expect:
        a.id != null

        when:
        a.delete()

        then:
        Project.get(pr.id) != null
    }
}
