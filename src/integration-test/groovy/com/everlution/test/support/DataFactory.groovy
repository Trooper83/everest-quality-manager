package com.everlution.test.support

import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.TestCycle
import com.github.javafaker.Faker

class DataFactory {

    static Faker faker = new Faker()

    static projectCodes = ["AAA", "AAB", "AAC", "AAD", "AAE", "AAF", "AAG", "AAH", "AAI", "AAJ", "AAK", "AAL", "AAM",
        "AAN", "AAO", "AAP", "AAQ", "AAR", "AAS", "AAT", "AAU", "AAV", "AAW", "AAX", "AAY", "AAZ", "ABA", "ACA",
        "ADA", "AEA", "AFA", "AGA", "AHA", "AIA", "AJA", "AKA", "ALA", "AMA", "ANA", "AOA", "APA", "AQA", "ARA",
        "ASA", "ATA", "AUA", "AVA", "AWA", "AXA", "AYA", "AZA", "BAA", "BAB", "BAC", "BAD", "BAE", "BAF", "BAG", "BAH",
        "BAI", "BAJ", "BAK", "BAL", "BAM", "BAN", "BAO", "BAP", "BAQ", "BAR", "BAS", "BAT", "BAU", "BAV", "BAW",
        "BAX", "BAY", "BAZ"]

    /**
     * creates fake data to populate an area
     */
    static Map<String, String> area() {
        return [name: faker.country().name()]
    }

    /**
     * creates fake data to populate a bug instance
     */
    static Map<String, String> bug() {
        def name = faker.zelda().game()
        def creator = faker.zelda().character()
        def description = faker.lorem().sentence(3)
        return [creator: creator, name: name, description: description]
    }

    /**
     * creates fake data to populate an environment
     */
    static Map<String, String> environment() {
        return [name: faker.lordOfTheRings().location()]
    }

    /**
     * creates fake data to populate a project instance
     */
    static Map<String, String> project() {
        def name = faker.lorem().sentence(2)
        def code = projectCodes.removeAt(0)
        return [name: name, code: code]
    }

    /**
     * creates fake data to populate a release plan instance
     */
    static Map<String, String> releasePlan() {
        return [name: faker.name().title()]
    }

    /**
     * creates fake data to populate a scenario instance
     */
    static Map<String, String> scenario() {
        def name = faker.zelda().game()
        def creator = faker.zelda().character()
        def description = faker.lorem().sentence(3)
        def gherkin = faker.lorem().sentence(5)
        return [name: name, creator: creator, description: description, gherkin: gherkin,
                executionMethod: "Automated", type: "UI"]
    }

    /**
     * creates fake data to populate a testCase instance
     */
    static Map<String, String> testCase() {
        def name = faker.zelda().game()
        def creator = faker.zelda().character()
        def description = faker.lorem().sentence(3)
        return [name: name, creator: creator, description: description, executionMethod: "Automated", type: "UI"]
    }

    /**
     * creates fake data to populate a testCycle instance
     * @return
     */
    static Map<String, String> testCycle() {
        return [name: faker.country().capital()]
    }

    /**
     * creates fake data to populate a test group instance
     */
    static Map<String, String> testGroup() {
        return [name: faker.name().title()]
    }

    static Project getProject() {
        Project.withNewSession { session ->
            new Project(name: faker.lorem().sentence(2), code: projectCodes.removeAt(0)).save()
        }
    }

    static ReleasePlan getReleasePlan() {
        ReleasePlan.withNewSession { session ->
            new ReleasePlan(name: faker.ancient().god(), project: getProject()).save()
        }
    }

    static TestCycle getTestCycle() {
        TestCycle.withNewSession { session ->
            def project = new Project(name: faker.lorem().sentence(2), code: projectCodes.removeAt(0)).save()
            def plan = new ReleasePlan(name: faker.ancient().god(), project: project).save()
            new TestCycle(name: faker.animal().name(), releasePlan: plan).save()
        }
    }
}
