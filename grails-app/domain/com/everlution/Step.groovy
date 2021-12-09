package com.everlution

class Step {

    String action
    Date dateCreated
    String result

    static belongsTo = [Bug, TestCase]

    static constraints = {
        action blank: true, maxSize: 500, nullable: true, validator: {
            val, Step obj ->
                if(val == null && obj.result == null) {
                    return false
            }
        }
        result blank: true, maxSize: 500, nullable: true, validator: {
            val, Step obj ->
                if(val == null && obj.action == null) {
                    return false
                }
        }
    }
}
