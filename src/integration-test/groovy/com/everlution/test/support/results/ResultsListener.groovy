package com.everlution.test.support.results

import com.everlution.TestRunResult
import org.spockframework.runtime.AbstractRunListener
import org.spockframework.runtime.model.ErrorInfo
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.IterationInfo

class ResultsListener extends AbstractRunListener {

    @Override
    void error(ErrorInfo error) {
        String cause
        try {
            cause = error.exception
        } catch (MissingPropertyException ignored) {
            cause = null
        }

        def name =
                "${error.method.parent.package}.${error.method.parent.name}.${error.method.name} _iteration_${error.method.iteration.iterationIndex}"
        ResultStore.addResult(new TestRunResult(testName: name, result: "Failed", failureCause: cause))
    }

    /**
     * according to spock documentation this should only run for data-driven tests but it runs for
     * all tests regardless. If this fails to run for non-data-driven tests use afterFeature()
     */
    @Override
    void afterIteration(IterationInfo iteration) {
        def name =
                "${iteration.parent.parent.package}.${iteration.parent.parent.name}.${iteration.name} _iteration_${iteration.iterationIndex}"
        if(!ResultStore.resultExists(name)) {
            ResultStore.addResult(new TestRunResult(testName: name, result: "Passed"))
        }
    }

    @Override
    void featureSkipped(FeatureInfo feature) {
        def name = "${feature.parent.package}.${feature.parent.name}.${feature.name}"
        ResultStore.addResult(new TestRunResult(testName: name, result: "SKIPPED"))
    }
}
