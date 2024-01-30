package com.everlution.command

import com.everlution.Project
import com.everlution.TestRunResult
import grails.validation.Validateable

class TestRunCmd implements Validateable {

    Project project
    String name
    List<TestRunResult> testResults
}
