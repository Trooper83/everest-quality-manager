package com.manager.quality.everest.test.api.support.services

import com.manager.quality.everest.test.api.support.models.LoginResponse
import com.manager.quality.everest.test.api.support.utils.TokenStore
import kong.unirest.Unirest

class AuthService {

    private String baseUrl

    AuthService(url) {
        this.baseUrl = url
    }

    def login(username, password) {
        def token = TokenStore.getToken(username)
        if(token) {
            return token
        }
        else {
            def r = Unirest.post(baseUrl + "/api/login")
                    .body("{\"username\": \"${username}\",\"password\": \"${password}\"}")
                    .asObject(LoginResponse)
            def t = r.getBody().access_token
            TokenStore.storeToken(username, t)
            return t
        }
    }
}
