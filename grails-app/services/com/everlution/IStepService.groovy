package com.everlution

interface IStepService {

    Step get(Serializable id)

    void delete(Serializable id)

    Step save(Step step)
}