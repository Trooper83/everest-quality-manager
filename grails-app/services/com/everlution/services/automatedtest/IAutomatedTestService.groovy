package com.everlution.services.automatedtest

import com.everlution.domains.AutomatedTest
import com.everlution.domains.Project

interface IAutomatedTestService {

    Long countByProject(Project project)

    AutomatedTest findByProjectAndFullName(Project project, String fullName)

    AutomatedTest get(Serializable id)

    AutomatedTest save(AutomatedTest automatedTest)
}