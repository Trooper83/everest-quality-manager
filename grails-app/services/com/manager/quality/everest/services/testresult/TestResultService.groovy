package com.manager.quality.everest.services.testresult

import com.manager.quality.everest.AutomatedTestResultsViewModel
import com.manager.quality.everest.domains.AutomatedTest
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.TestResult
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
        def totalPass = results.findAll { it -> it.result == 'PASSED' }
        def totalFail = results.findAll { it -> it.result == 'FAILED' }
        def totalSkip = results.findAll { it -> it.result == 'SKIPPED' }
        def recentResults = []
        if(total <= 20) {
            recentResults = results
        } else {
            recentResults = results[(total - 20)..(total - 1)]
        }
        def recentTotal = recentResults.size()
        def recentTotalPass = recentResults.findAll { it -> it.result == 'PASSED' }
        def recentTotalFail = recentResults.findAll { it -> it.result == 'FAILED' }
        def recentTotalSkip = recentResults.findAll { it -> it.result == 'SKIPPED' }
        def values = new AutomatedTestResultsViewModel(total, totalPass.size(), totalFail.size(), totalSkip.size(),
                recentTotal, recentTotalPass.size(), recentTotalFail.size(), recentTotalSkip.size(), recentResults)
        return values
    }

    List getMostFailedTestCount(Project project) {
        def r = TestResult.findAll("select t.automatedTest.id, t.automatedTest.fullName, count(*) as c from TestResult t " +
                "where t.result = ?0 and t.automatedTest.project = ?1 group by t.automatedTest order by c desc",
                ['FAILED', project], [max:10])
        return r
    }
}
