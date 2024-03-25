package com.everlution.domains

class StepTemplate {

    String act
    Date dateCreated
    Date lastUpdated
    String name
    Person person
    Project project
    String result

    static mapping = {
        person cascade: "none"
        project cascade: "none"
    }

    static constraints = {
        act blank: true, maxSize: 500, nullable: true, validator: {
            val, StepTemplate obj ->
                if(val == null && obj.result == null) {
                    return false
                }
        }
        name blank: false, nullable: false, maxSize: 255
        person nullable: false
        project nullable: false
        result blank: true, maxSize: 500, nullable: true, validator: {
            val, StepTemplate obj ->
                if(val == null && obj.act == null) {
                    return false
                }
        }
    }
}
