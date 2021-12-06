package com.everlution.test.ui.support.data

enum Usernames {
    READ_ONLY("read_only@readonly.com"), BASIC("basic@basic.com"),
    PROJECT_ADMIN("project_admin@projectadmin.com"), APP_ADMIN("app_admin@appadmin.com"),
    ORG_ADMIN("org_admin@orgadmin.com")

    Usernames(String username) {
        this.username = username
    }

    private final String username
    String getUsername() {
        username
    }
}