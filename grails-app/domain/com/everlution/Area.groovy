package com.everlution

class Area {

    String name

    static belongsTo = [Project]

    static constraints = {
        name blank: false, maxSize: 100, nullable: false
    }
}
