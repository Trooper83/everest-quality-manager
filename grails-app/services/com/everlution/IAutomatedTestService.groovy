package com.everlution

interface IAutomatedTestService {

    AutomatedTest findByProjectAndFullName(Project project, String fullName)

    AutomatedTest save(AutomatedTest automatedTest)
}