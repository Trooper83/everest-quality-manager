package com.everlution.test.testgroup

import com.everlution.Project
import com.everlution.TestGroup
import grails.test.hibernate.HibernateSpec
import spock.lang.Shared

class TestGroupHibernateSpec extends HibernateSpec {

    @Shared Project project

    def setup() {
        project = new Project(name: "Test Group Project", code: "TGP").save()
    }

    void "test date created auto generated"() {
        when:
        def group = new TestGroup(name: "name", project: project).save()

        then:
        group.dateCreated != null
    }
}
