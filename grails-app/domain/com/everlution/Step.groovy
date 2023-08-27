package com.everlution

class Step {

    String act
    Date dateCreated
    boolean isBuilderStep = false
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
            val, Step obj ->
                if(val == null && obj.result == null) {
                    return false
            }
        }
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
                if(val == null && obj.act == null) {
                    return false
                }
        }
    }
}
