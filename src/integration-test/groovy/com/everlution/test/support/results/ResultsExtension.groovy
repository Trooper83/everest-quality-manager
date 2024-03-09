package com.everlution.test.support.results

import org.spockframework.runtime.extension.IAnnotationDrivenExtension
import org.spockframework.runtime.model.SpecInfo

class ResultsExtension implements IAnnotationDrivenExtension<SendResults> {

    @Override
    void visitSpecAnnotation(SendResults annotations, SpecInfo spec) {
        if(System.getProperty("sendResults") == "true") {
            spec.addListener(new ResultsListener())
        }
    }
}
