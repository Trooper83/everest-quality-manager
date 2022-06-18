package com.everlution.test.ui.support.data

enum UserStatuses {
    ACCOUNT_EXPIRED("accountExpired"), ACCOUNT_LOCKED("accountLocked"), ENABLED("enabled"),
    PASSWORD_EXPIRED("passwordExpired")

    UserStatuses(String status) {
        this.status = status
    }

    private final String status
    String getStatus() {
        return status
    }
}