package com.everlution.enums

enum StepsFormat {
    GHERKIN('Gherkin'), STANDARD('Standard')

    private final String format

    private StepsFormat(String format) {
        this.format = format
    }
}