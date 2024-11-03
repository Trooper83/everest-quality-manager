package com.manager.quality.everest

import com.manager.quality.everest.domains.TestRun

class RunWithPaginatedResults {

    TestRun testRun
    List results

    RunWithPaginatedResults(TestRun testRun, List results) {
        this.testRun = testRun
        this.results = results
    }
}
