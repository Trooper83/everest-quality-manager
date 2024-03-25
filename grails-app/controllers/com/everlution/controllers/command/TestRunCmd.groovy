package com.everlution.controllers.command

import com.everlution.TestRunResult
import com.everlution.domains.Project
import grails.validation.Validateable

class TestRunCmd implements Validateable {

    Project project
    String name
    List<TestRunResult> testResults
}
