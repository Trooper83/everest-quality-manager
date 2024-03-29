package com.everlution.domains

class IterationStep {

    String act
    String data
    String result

    static belongsTo = [ TestIteration ]

    static constraints = {
        act blank: true, maxSize: 500, nullable: true, validator: {
            val, IterationStep obj ->
                if(val == null && obj.result == null) {
                    return false
                }
        }
        data blank: true, nullable: true, maxSize: 500
        result blank: true, maxSize: 500, nullable: true, validator: {
            val, IterationStep obj ->
                if(val == null && obj.act == null) {
                    return false
                }
        }
    }
}
