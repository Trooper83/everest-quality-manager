package com.everlution.test.ui.support.data

enum Usernames {
    ACCOUNT_EXPIRED("expired@accountexpired.com"), ACCOUNT_LOCKED("locked@accountlocked.com"),
    APP_ADMIN("app_admin@appadmin.com"), BASIC("basic@basic.com"),
    DISABLED("disabled@disabled.com"), ORG_ADMIN("org_admin@orgadmin.com"),
    PASSWORD_LOCKED("locked@passwordlocked.com"), PROJECT_ADMIN("project_admin@projectadmin.com"),
    READ_ONLY("read_only@readonly.com")


    Usernames(String username) {
        this.username = username
    }

    private final String username
    String getUsername() {
        username
    }
}