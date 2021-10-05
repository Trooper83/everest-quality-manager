package com.everlution

import grails.test.hibernate.HibernateSpec

import javax.persistence.PersistenceException

class ProjectHibernateSpec extends HibernateSpec {

    List<Class> getDomainClasses() { [Project] }

    void "project name must be unique"() {
        given: "a project instance"
        new Project(name: "test", code: "cod").save()

        when: "save a project with same name"
        def failed = new Project(name: "test", code: "san")

        then: "name fails validation"
        !failed.validate(["name"])

        and: "unique error"
        failed.errors["name"].code == "unique"

        and: "save fails"
        !failed.save()
        Project.count() == old(Project.count())
    }

    void "project code must be unique"() {
        given: "a project instance"
        new Project(name: "test", code: "cod").save()

        when: "save a project with same code"
        def failed = new Project(name: "testing", code: "cod")

        then: "code fails validation"
        !failed.validate(["code"])

        and: "unique error"
        failed.errors["code"].code == "unique"

        and: "save fails"
        !failed.save()
        Project.count() == old(Project.count())
    }

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
}
