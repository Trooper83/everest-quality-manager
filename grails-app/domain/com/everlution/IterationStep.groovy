package com.everlution

class IterationStep {

    String action
    String result

    static belongsTo = [ TestIteration ]

    static constraints = {
        action blank: true, maxSize: 500, nullable: true, validator: {
            val, IterationStep obj ->
                if(val == null && obj.result == null) {
                    return false
                }
        }
        result blank: true, maxSize: 500, nullable: true, validator: {
            val, IterationStep obj ->
                if(val == null && obj.action == null) {
                    return false
                }
        }
    }
}
