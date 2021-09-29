package com.everlution

import grails.test.hibernate.HibernateSpec
import org.springframework.dao.InvalidDataAccessApiUsageException

class BugHibernateSpec extends HibernateSpec {

    void "date created populates on save"() {
        given: "a bug instance"
        def project = new Project(name: "tc domain project", code: "tdp").save()
        def bug = new Bug(creator: "test", name: "First Bug", description: "test", project: project).save()

        expect: "bug has date created"
        bug.dateCreated != null
    }

    void "test save does not cascade to project"() {
        when: "unsaved project is added to bug"
        Project project = new Project(name: "BugServiceSpec Project2", code: "BMP")
        new Bug(creator: "Athena", description: "Found a bug123", name: "Name of the bug123", project: project).save()

        then: "exception is thrown"
        thrown(InvalidDataAccessApiUsageException)
    }

    void "delete bug does not cascade to project"() {
        given: "valid project and bug"
        Project project = new Project(name: "Delete Bug Cascade Project", code: "ZZ8").save()
        Bug bug = new Bug(name: "cascade project", description: "this should delete", creator: "testing",
                project: project).save()

        expect:
        project.id != null
        bug.id != null

        when: "delete bug"
        bug.delete()
        sessionFactory.currentSession.flush()

        then: "project is not deleted"
        Project.findById(project.id) != null
    }
}
