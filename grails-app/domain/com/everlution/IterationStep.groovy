package com.everlution

class IterationStep {

    String act
    String result

    static belongsTo = [ TestIteration ]

    static constraints = {
        act blank: true, maxSize: 500, nullable: true, validator: {
            val, IterationStep obj ->
                if(val == null && obj.result == null) {
                    return false
                }
        }
        result blank: true, maxSize: 500, nullable: true, validator: {
            val, IterationStep obj ->
                if(val == null && obj.act == null) {
                    return false
                }
        }
    }
}
