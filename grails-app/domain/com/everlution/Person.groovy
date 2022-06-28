package com.everlution

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='email')
@ToString(includes='email', includeNames=true, includePackage=false)
class Person implements Serializable {

    private static final long serialVersionUID = 1

    String email
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Set<Role> getAuthorities() {
        (PersonRole.findAllByPerson(this) as List<PersonRole>)*.role as Set<Role>
    }

    static constraints = {
        password nullable: false, blank: false, password: true,
                matches: "^.*(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%^&]).*\$", size: 8..256
        email nullable: false, blank: false, unique: true, email: true
    }

    static mapping = {
	    password column: '`password`'
    }
}
