package com.manager.quality.everest.test.support.data

enum SecurityRoles {

    ROLE_APP_ADMIN("ROLE_APP_ADMIN"), ROLE_BASIC("ROLE_BASIC"), ROLE_PROJECT_ADMIN("ROLE_PROJECT_ADMIN"),
    ROLE_READ_ONLY("ROLE_READ_ONLY")

    SecurityRoles(String role) {
        this.role = role
    }

    private final String role
    String getRole() {
        return role
    }

}