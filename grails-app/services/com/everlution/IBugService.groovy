package com.everlution

interface IBugService {

    Bug get(Serializable id)

    List<Bug> list(Map args)

    Long count()

    void delete(Serializable id)

    Bug read(Serializable id)

    Bug save(Bug bug)

}