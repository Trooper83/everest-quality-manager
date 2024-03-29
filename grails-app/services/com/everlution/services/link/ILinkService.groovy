package com.everlution.services.link

import com.everlution.domains.Link

interface ILinkService {

    void delete(Serializable id)

    Link read(Serializable id)

    Link save(Link link)
}