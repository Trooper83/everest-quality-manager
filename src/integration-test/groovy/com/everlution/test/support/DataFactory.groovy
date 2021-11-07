package com.everlution.test.support

import com.github.javafaker.Faker

class DataFactory {

    static Faker faker = new Faker()

    static projectCodes = ["AAA", "AAB", "AAC", "AAD", "AAE", "AAF", "AAG", "AAH", "AAI", "AAJ", "AAK", "AAL", "AAM",
        "AAN", "AAO", "AAP", "AAQ", "AAR", "AAS", "AAT", "AAU", "AAV", "AAW", "AAX", "AAY", "AAZ"]

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
     * creates fake data to populate a project instance
     */
    static Map<String, String> project() {
        def name = faker.lorem().sentence(3)
        def code = projectCodes.removeAt(0)
        return [name: name, code: code]
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
}