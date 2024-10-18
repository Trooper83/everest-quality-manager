package com.manager.quality.everest

import com.manager.quality.everest.domains.TestGroup

class GroupWithPaginatedTests {

    TestGroup testGroup
    List tests

    GroupWithPaginatedTests(TestGroup testGroup, List tests) {
        this.testGroup = testGroup
        this.tests = tests
    }
}
