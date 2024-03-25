package com.everlution.services.person

import grails.gorm.transactions.Transactional

/**
 * sub-class to implement necessary functions due to using 'person' in lieu of 'user'
 * for security plugin to avoid postgres reserved keyword clash
 */
class SpringSecurityUiService extends grails.plugin.springsecurity.ui.SpringSecurityUiService {

    @Override
    @Transactional
    void addUserRoles(user, Set rolesToAdd) {
        if (!user || !rolesToAdd) {
            return
        }
        rolesToAdd.each { role ->
            def instance = PersonRole.getDeclaredConstructor().newInstance()
            instance.person = user
            instance.role = role
            instance.save(insert: true)
            instance
        }
    }

    @Override
    @Transactional
    Number removeUserRole(def p, def r) {
        PersonRole.where { person == p && role == r }.deleteAll()
    }
}
