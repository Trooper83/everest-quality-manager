package com.everlution.test.ui.support.data

enum SecurityRoles {

    ROLE_APP_ADMIN("ROLE_APP_ADMIN"), ROLE_BASIC("ROLE_BASIC"), ROLE_ORG_ADMIN("ROLE_ORG_ADMIN"),
    ROLE_PROJECT_ADMIN("ROLE_PROJECT_ADMIN"), ROLE_READ_ONLY("ROLE_READ_ONLY")

    SecurityRoles(String role) {
        this.role = role
    }

    private final String role
    String getRole() {
        return role
    }

}