package com.everlution

class Project {

    String name
    String code

    static constraints = {
        name blank: false, nullable: false, maxSize: 100, unique: true
        code blank: false, nullable: false, minSize: 3, maxSize: 3, unique: true
    }
}
