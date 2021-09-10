package com.everlution

import grails.test.hibernate.HibernateSpec

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
}
