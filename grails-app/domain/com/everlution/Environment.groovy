package com.everlution

class Environment {

    String name

    static belongsTo = [Project]

    static constraints = {
        name blank: false, maxSize: 255, nullable: false
    }
}
