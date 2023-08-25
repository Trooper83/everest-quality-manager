package com.everlution

interface IStepService {

    Step get(Serializable id)

    Step read(Serializable id)

    Step save(Step step)
}