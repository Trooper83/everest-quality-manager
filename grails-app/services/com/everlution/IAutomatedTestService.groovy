package com.everlution

interface IAutomatedTestService {

    AutomatedTest findByProjectAndFullName(Project project, String fullName)

    AutomatedTest get(Serializable id)

    AutomatedTest save(AutomatedTest automatedTest)
}