package com.manager.quality.everest.domains

class Step {

    String act
    String data
    Date dateCreated
    boolean isBuilderStep = false
    Date lastUpdated
    String result
    StepTemplate template

    static belongsTo = [TestCase, Bug]

    static mapping = {
        template cascade: "none"
    }

    static constraints = {
        act blank: true, maxSize: 500, nullable: true, validator: {
            val, Step obj ->
                if(val == null && obj.result == null) {
                    return false
            }
        }
        data blank: true, nullable: true, maxSize: 500
        result blank: true, maxSize: 500, nullable: true, validator: {
            val, Step obj ->
                if(val == null && obj.act == null) {
                    return false
                }
        }
        template nullable: true
    }
}
