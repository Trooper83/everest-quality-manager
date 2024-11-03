package com.manager.quality.everest.controllers.command

import com.manager.quality.everest.TestRunResult
import com.manager.quality.everest.domains.Project
import grails.validation.Validateable

class TestRunCmd implements Validateable {

    Project project
    String name
    List<TestRunResult> testResults
}
