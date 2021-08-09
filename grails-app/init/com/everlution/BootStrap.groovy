package com.everlution

import grails.gorm.transactions.Transactional

class BootStrap {

    def init = { servletContext ->
        environments {
            test {

            }
            development {
                addTestUser()
                seedTestCases()
            }
        }
    }

    def destroy = {
    }

    @Transactional
    void addTestUser() {
        def basicRole = new Role(authority: 'ROLE_BASIC').save(failOnError: true)

        def testUser = new Person(username: 'test', password: 'password').save(failOnError: true)

        PersonRole.create(testUser, basicRole)

        PersonRole.withSession {
            it.flush()
            it.clear()
        }
    }

    void seedTestCases() {
        def s = new TestStep(action: "do something", result: "expect something").save(failOnError: true)
        new TestCase(creator: "test", name: "everest", description: "lorem ipsum etc...",
                executionMethod: "manual", type: "ui", steps: s).save(failOnError: true)
    }
}
