package com.everlution

interface ILinkService {

    void delete(Serializable id)

    Link read(Serializable id)

    Link save(Link link)
}