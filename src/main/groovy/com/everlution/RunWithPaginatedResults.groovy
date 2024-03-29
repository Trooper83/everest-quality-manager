package com.everlution

import com.everlution.domains.TestRun

class RunWithPaginatedResults {

    TestRun testRun
    List results

    RunWithPaginatedResults(TestRun testRun, List results) {
        this.testRun = testRun
        this.results = results
    }
}
