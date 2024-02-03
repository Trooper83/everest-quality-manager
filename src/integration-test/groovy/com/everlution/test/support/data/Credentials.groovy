package com.everlution.test.support.data

enum Credentials {
    ACCOUNT_EXPIRED("expired@accountexpired.com", "!Password#2022"),
    ACCOUNT_LOCKED("locked@accountlocked.com", "!Password#2022"),
    APP_ADMIN("app_admin@appadmin.com", "!Password#2022"),
    BASIC("basic@basic.com", "!Password#2022"),
    DISABLED("disabled@disabled.com", "!Password#2022"),
    PASSWORD_EXPIRED("expired@passwordexpired.com", "!Password#2022"),
    PROJECT_ADMIN("project_admin@projectadmin.com", "!Password#2022"),
    READ_ONLY("read_only@readonly.com", "!Password#2022")


    Credentials(String email, String password) {
        this.email = email
        this.password = password
    }

    private final String password
    private final String email

    String getPassword() {
        password
    }

    String getEmail() {
        email
    }
}