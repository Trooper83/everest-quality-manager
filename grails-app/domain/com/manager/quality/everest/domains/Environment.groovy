package com.manager.quality.everest.domains

class Environment {

    String name

    static belongsTo = [Project]

    static constraints = {
        name blank: false, maxSize: 100, nullable: false
    }
}
