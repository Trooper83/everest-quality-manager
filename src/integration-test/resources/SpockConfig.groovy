import org.spockframework.runtime.model.parallel.ExecutionMode

runner {
    parallel {
        enabled false
        fixed(2)
        defaultSpecificationExecutionMode = ExecutionMode.CONCURRENT
        defaultExecutionMode = ExecutionMode.SAME_THREAD
    }
}