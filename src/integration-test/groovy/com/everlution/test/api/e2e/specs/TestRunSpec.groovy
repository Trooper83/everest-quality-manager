package com.everlution.test.api.e2e.specs

import kong.unirest.Unirest
import spock.lang.Specification

class TestRunSpec extends Specification {

    void "201 when test run created no results"() {
        when:
        def r = Unirest.post("http://localhost:8080/api/testRuns")
                .header("Content-Type", "application/json")
                .body("{\"project\": \"1\", \"name\": \"testing\"}")
                .asString()

        then:
        r.status == 201
        r.body ==~ /TestRun \d+ created/
    }
}
