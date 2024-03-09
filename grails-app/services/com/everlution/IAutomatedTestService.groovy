package com.everlution

interface IAutomatedTestService {

    Long countByProject(Project project)

    AutomatedTest findByProjectAndFullName(Project project, String fullName)

    AutomatedTest get(Serializable id)

    AutomatedTest save(AutomatedTest automatedTest)
}