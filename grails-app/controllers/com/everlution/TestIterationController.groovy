package com.everlution

import grails.plugin.springsecurity.annotation.Secured

class TestIterationController {

    TestIterationService testIterationService

    /**
     * displays the show view
     * /testIteration/show/${id}
     * @param id - id of the instance to display
     */
    @Secured("ROLE_READ_ONLY")
    def show(Long id) {
        respond testIterationService.get(id), view: 'show'
    }
}
