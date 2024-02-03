package com.everlution.test.api.functional.specs

import com.everlution.test.support.data.Credentials
import grails.testing.mixin.integration.Integration
import kong.unirest.Unirest
import spock.lang.Shared
import spock.lang.Specification

@Integration
class LoginSpec extends Specification {

    @Shared String url

    def setup() {
        url = "http://localhost:${serverPort}/api/login"
    }

    void "authorized user gets token"() {
        when:
        def r = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body("{\"username\": \"${Credentials.BASIC.email}\",\"password\": \"${Credentials.BASIC.password}\"}")
                .asJson()

        then:
        r.status == 200
        r.body != null
    }

    void "unauthorized user gets 401"(String username, String password) {
        when:
        def r = Unirest.post(url)
                .body("{\"username\": \"${username}\",\"password\": \"${password}\"}")
                .asEmpty()

        then:
        r.status == 401

        where:
        username          | password
        'email@email.com' | '!Password#48575'
        'email@email'     | '!Password#48575'
        'email@email.com' | 'password'
    }

    void "user not authorized when username password blank or null"(String username, String password, int code) {
        when:
        def r = Unirest.post(url)
                .body("{\"username\": \"${username}\",\"password\": \"${password}\"}")
                .asEmpty()

        then:
        r.status == code

        where:
        username          | password          | code
        null              | '!Password#48575' | 401
        ''                | '!Password#48575' | 400
        'email@email'     | null              | 401
        'email@email.com' | ''                | 400
    }
}
