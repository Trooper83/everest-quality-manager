package com.everlution.test.support.results

import com.everlution.TestRunResult

class ResultStore {

    static private List<TestRunResult> results = []
    static private boolean sent = false

    static void addResult(TestRunResult result) {
        results.add(result)
    }

    static List<TestRunResult> getResults() {
        return results
    }

    static boolean getSent() {
        return sent
    }

    static boolean resultExists(String name) {
        def found = results.find { it -> it.testName == name }
        return found != null
    }

    static void setSent() {
        sent = true
    }
}
