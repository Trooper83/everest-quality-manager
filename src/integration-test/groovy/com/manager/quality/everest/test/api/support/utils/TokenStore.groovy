package com.manager.quality.everest.test.api.support.utils

class TokenStore {

    private static Map<String, String> tokens = [:]

    static getToken(username) {
        if(tokens.containsKey(username)) {
            return tokens.get(username)
        }
        return null
    }

    static storeToken(String username, String token) {
        tokens.put(username, token)
    }
}
