package com.manager.quality.everest

enum Relationship {

    IS_CHILD_OF('Is Child of'),
    IS_PARENT_OF('Is Parent of'),
    IS_SIBLING_OF('Is Sibling of')

    Relationship(String name) {
        this.name = name
    }

    private final String name

    String getName() {
        name
    }
}