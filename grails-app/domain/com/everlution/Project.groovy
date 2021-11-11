package com.everlution

class Project {

    List areas
    String code
    String name

    static hasMany = [areas: Area, bugs: Bug, testCases: TestCase]

    static mapping = {
        areas cascade: "all-delete-orphan"
    }

    static constraints = {
        areas validator: { val ->
            if(val == null) {
                return true
            }
            def duplicates = val.countBy{it.name}.grep{it.value > 1}.collect{it.key}
            if(duplicates.size() > 0) {
                return false
            }
            return true
        }
        code blank: false, nullable: false, minSize: 3, maxSize: 3, unique: true
        name blank: false, nullable: false, maxSize: 100, unique: true
    }
}
