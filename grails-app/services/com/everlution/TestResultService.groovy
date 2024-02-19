package com.everlution

import grails.gorm.services.Service

@Service(TestResult)
abstract class TestResultService implements ITestResultService {

    /**
     * gets recent results [20] for an automated test with pass fail skip stats
     * @param automatedTest
     * @return
     */
    AutomatedTestResultsViewModel getResultsForAutomatedTest(AutomatedTest automatedTest) {
        def results = findAllByAutomatedTest(automatedTest).sort { it.dateCreated }
        def total = results.size()
        def totalPass = results.findAll { it -> it.result == 'Passed' }
        def totalFail = results.findAll { it -> it.result == 'Failed' }
        def totalSkip = results.findAll { it -> it.result == 'Skipped' }
        def recentResults = []
        if(total <= 20) {
            recentResults = results
        } else {
            recentResults = results[(total - 20)..(total - 1)]
        }
        def recentTotal = recentResults.size()
        def recentTotalPass = recentResults.findAll { it -> it.result == 'Passed' }
        def recentTotalFail = recentResults.findAll { it -> it.result == 'Failed' }
        def recentTotalSkip = recentResults.findAll { it -> it.result == 'Skipped' }
        def values = new AutomatedTestResultsViewModel(total, totalPass.size(), totalFail.size(), totalSkip.size(),
                recentTotal, recentTotalPass.size(), recentTotalFail.size(), recentTotalSkip.size(), recentResults)
        return values
    }
}
