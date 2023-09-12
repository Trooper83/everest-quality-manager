package com.everlution

class LinkItem {

    LinkItem(Long id, Step step) {
        this.linkId = id
        this.linkedItem = step
    }

    Long linkId
    Step linkedItem
}
