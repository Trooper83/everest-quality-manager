package com.manager.quality.everest.controllers

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured

class UserController extends grails.plugin.springsecurity.ui.UserController {

    SpringSecurityService springSecurityService

    /**
     * renders the user's profile
     */
    @Secured('IS_AUTHENTICATED_FULLY')
    def profile() {
        def instance = springSecurityService.getCurrentUser()
        if (!instance) return

        model instance, 'update'
    }

    /**
     * updates the user's password
     */
    @Secured('IS_AUTHENTICATED_FULLY')
    def updatePassword() {
        withForm {
            if (params.password != params.confirmPassword) {
                flash.error = "Passwords must match."
                def instance = springSecurityService.getCurrentUser()
                render view:'profile', model: model(instance, 'update')
                return
            }
            doUpdatePassword { user ->
                uiUserStrategy.updateUser params, user, roleNamesFromParams()
            }
        }.invalidToken {
            flash.error = 'An error has occurred, please try again.'
            response.status = 500
            redirect action: 'profile'
        }
    }

    /**
     * helper action to update a user's password
     */
    private doUpdatePassword(Closure update) {
        def instance = lookupFromParams()
        if (!instance || !versionCheck(instance)) {
            return
        }

        update instance

        if (instance.hasErrors()) {
            render view:'profile', model: model(instance, 'update')
        }
        else {
            flash.message = 'Password updated.'
            render view: 'profile', model: model(instance, 'update')
        }
    }

    /**
     * searches for user's in the app
     */
    @Secured('ROLE_APP_ADMIN')
    @Override
    def search() {
        if (!isSearch()) {
            // show the form
            return
        }

        def results = doSearch { ->
            like 'email', delegate
            eqBoolean 'accountExpired', delegate
            eqBoolean 'accountLocked', delegate
            eqBoolean 'enabled', delegate
            eqBoolean 'passwordExpired', delegate
        }

        renderSearch results: results, totalCount: results.totalCount,
                'accountExpired', 'accountLocked', 'enabled', 'passwordExpired', 'email'
    }
}
