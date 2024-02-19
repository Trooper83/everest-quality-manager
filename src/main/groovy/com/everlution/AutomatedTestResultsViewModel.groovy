package com.everlution

class AutomatedTestResultsViewModel {

    final int total
    final int passTotal
    final int failTotal
    final int skipTotal
    final int recentTotal
    final int recentPassTotal
    final int recentFailTotal
    final int recentSkipTotal
    final List recentResults
    
    AutomatedTestResultsViewModel(total, totalPassed, totalFailed, totalSkipped, recentTotal,
            recentTotalPassed, recentTotalFailed, recentTotalSkipped, recentResults) {
        this.total = total
        this.passTotal = totalPassed
        this.failTotal = totalFailed
        this.skipTotal = totalSkipped
        this.recentTotal = recentTotal
        this.recentPassTotal = recentTotalPassed
        this.recentFailTotal = recentTotalFailed
        this.recentSkipTotal = recentTotalSkipped
        this.recentResults = recentResults
    }
}
