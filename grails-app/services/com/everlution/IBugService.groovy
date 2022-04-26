package com.everlution

interface IBugService {

    Long count()

    void delete(Serializable id)

    Bug get(Serializable id)

    List<Bug> list(Map args)

    Bug read(Serializable id)

    Bug save(Bug bug)

}