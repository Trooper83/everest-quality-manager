package com.everlution

import com.everlution.domains.TestGroup

class GroupWithPaginatedTests {

    TestGroup testGroup
    List tests

    GroupWithPaginatedTests(TestGroup testGroup, List tests) {
        this.testGroup = testGroup
        this.tests = tests
    }
}
