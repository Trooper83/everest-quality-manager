package com.everlution

class Step {

    String action
    Date dateCreated
    String result

    static constraints = {
        action blank: true, maxSize: 500, nullable: false
        result blank: true, maxSize: 500, nullable: false
    }
}
