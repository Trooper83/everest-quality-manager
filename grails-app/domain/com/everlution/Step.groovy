package com.everlution

class Step {

    String action
    Date dateCreated
    boolean isBuilderStep = false
    Collection linkedSteps
    String name
    Person person
    Project project
    String result

    static hasMany = [ linkedSteps: Step ]

    static mapping = {
        linkedSteps cascade: "none"
    }

    static constraints = {
        action blank: true, maxSize: 500, nullable: true, validator: {
            val, Step obj ->
                if(val == null && obj.result == null) {
                    return false
            }
        }
        linkedSteps nullable: true
        name blank: true, maxSize: 255, nullable: true, validator: {
            val, Step obj ->
                if(val == null && obj.isBuilderStep == true) {
                    return false
                }
        }
        person nullable: false
        project nullable: false
        result blank: true, maxSize: 500, nullable: true, validator: {
            val, Step obj ->
                if(val == null && obj.action == null) {
                    return false
                }
        }
    }
}
