package com.manager.quality.everest

import com.manager.quality.everest.domains.StepTemplate

class LinkItem {

    LinkItem(Long id, StepTemplate template) {
        this.linkId = id
        this.linkedItem = template
    }

    Long linkId
    StepTemplate linkedItem
}
