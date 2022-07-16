package com.everlution

import grails.databinding.BindUsing

class Project {

    List areas
    @BindUsing({obj, source ->
        source['code'].toUpperCase()
    })
    String code
    List environments
    @BindUsing({obj, source ->
        source['name'].capitalize()
    })
    String name
    Collection testGroups

    static hasMany = [ areas: Area, environments: Environment, testGroups: TestGroup ]

    static mapping = {
        areas cascade: "all-delete-orphan"
        environments cascade: "all-delete-orphan"
        testGroups cascade: "all-delete-orphan"
    }

    static constraints = {
        code blank: false, nullable: false, minSize: 3, maxSize: 3, unique: true
        name blank: false, nullable: false, maxSize: 100, unique: true
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
        environments validator: { val ->
            if(val == null) {
                return true
            }
            def duplicates = val.countBy{it.name}.grep{it.value > 1}.collect{it.key}
            if(duplicates.size() > 0) {
                return false
            }
            return true
        }
        testGroups validator: { val ->
            if(val == null) {
                return true
            }
            def duplicates = val.countBy{it.name}.grep{it.value > 1}.collect{it.key}
            if(duplicates.size() > 0) {
                return false
            }
            return true
        }
    }
}
