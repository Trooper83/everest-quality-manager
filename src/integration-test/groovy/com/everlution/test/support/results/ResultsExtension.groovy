package com.everlution.test.support.results

import org.spockframework.runtime.extension.IAnnotationDrivenExtension
import org.spockframework.runtime.model.SpecInfo

class ResultsExtension implements IAnnotationDrivenExtension<SendResults> {

    @Override
    void visitSpecAnnotation(SendResults annotations, SpecInfo spec) {
        spec.addListener(new ResultsListener())
    }
}
