package com.manager.quality.everest.services.automatedtest

import com.manager.quality.everest.domains.AutomatedTest
import com.manager.quality.everest.domains.Project

interface IAutomatedTestService {

    Long countByProject(Project project)

    AutomatedTest findByProjectAndFullName(Project project, String fullName)

    AutomatedTest get(Serializable id)

    AutomatedTest save(AutomatedTest automatedTest)
}