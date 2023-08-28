package com.everlution

interface IBugService {

    void delete(Serializable id)

    Bug get(Serializable id)

    int countByProject(Project project)

    Bug read(Serializable id)

    Bug save(Bug bug)

}