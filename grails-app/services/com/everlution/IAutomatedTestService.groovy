package com.everlution

interface IAutomatedTestService {

    List<AutomatedTest> findAllByProject(Project project)

    AutomatedTest findByProjectAndFullName(Project project, String fullName)

    AutomatedTest save(AutomatedTest automatedTest)
}