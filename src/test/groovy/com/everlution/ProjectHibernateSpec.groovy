package com.everlution

import grails.test.hibernate.HibernateSpec

import javax.persistence.PersistenceException

class ProjectHibernateSpec extends HibernateSpec {

    List<Class> getDomainClasses() { [Project] }

    void "delete project with bug throws persistence exception"() {
        given: "valid bug with a project"
        Project project = new Project(name: "Delete Bug Cascade Project777", code: "ZZ7").save()
        new Bug(name: "cascade project", description: "this should delete", creator: "testing",
                project: project).save()

        when: "delete project"
        project.delete()
        sessionFactory.currentSession.flush()

        then: "exception is thrown"
        thrown(PersistenceException)
    }

    void "delete project with test case throws persistence exception"() {
        given: "valid test case with project"
        Project project = new Project(name: "Delete Test Case Cascade Project000", code: "ZZ0").save()
        new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        when: "delete project"
        project.delete()
        sessionFactory.currentSession.flush()

        then: "exception is thrown"
        thrown(PersistenceException)
    }

    void "delete project cascades to area"() {
        given: "project with valid area params"
        Project p = new Project(name: "Cascades To Area", code: "CTA", areas: [new Area(name: "area name")]).save()

        expect: "area is saved"
        Area.count() == 1

        when: "delete project"
        p.delete()
        sessionFactory.currentSession.flush()

        then: "area was deleted"
        Area.count() == 0
    }

    void "update project cascades to area"() {
        given: "project with valid area params"
        Project p = new Project(name: "Cascades To Area", code: "CTA", areas: [new Area(name: "area name")]).save()

        expect: "area is saved"
        p.areas[0].name == "area name"

        when: "update area"
        p.areas[0].name = "edited name"
        p.save()
        sessionFactory.currentSession.flush()

        then: "area was updated"
        Project.findById(p.id).areas[0].name == "edited name"
    }

    void "area order persists"() {
        given: "save a project with areas"
        def area1 = new Area(name: "area name1");
        def area2 = new Area(name: "area name2");
        def area3 = new Area(name: "area name3");
        Project project = new Project(name: "TestStep Cascade Project669", code: "TC2",
            areas: [area1, area2, area3]).save()

        when: "get the project"
        def p = Project.findById(project.id)

        then: "order is the same as when it was created"
        p.areas[0].id == area1.id
        p.areas[1].id == area2.id
        p.areas[2].id == area3.id
    }
}
