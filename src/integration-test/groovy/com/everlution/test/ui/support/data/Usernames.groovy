package com.everlution.test.ui.support.data

enum Usernames {
    READ_ONLY("read_only"), BASIC("basic"), PROJECT_ADMIN("project_admin"),
    APP_ADMIN("app_admin"), ORG_ADMIN("org_admin")

    Usernames(String username) {
        this.username = username
    }

    private final String username
    String getUsername() {
        username
    }
}