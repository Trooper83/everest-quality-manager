package com.manager.quality.everest.services.link

import com.manager.quality.everest.domains.Link

interface ILinkService {

    void delete(Serializable id)

    Link read(Serializable id)

    Link save(Link link)
}