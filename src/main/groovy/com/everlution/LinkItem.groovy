package com.everlution

class LinkItem {

    LinkItem(Long id, StepTemplate template) {
        this.linkId = id
        this.linkedItem = template
    }

    Long linkId
    StepTemplate linkedItem
}
